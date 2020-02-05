package tech.builtrix;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tech.builtrix.base.ErrorMessage;
import tech.builtrix.commons.LocalizationService;
import tech.builtrix.exceptions.ExceptionBase;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private LocalizationService localizationService;

	@ExceptionHandler(value = ExceptionBase.class)
	@ResponseBody
	ResponseEntity handleDomainException(ExceptionBase ex) {
		ErrorMessage errorMessage = ex.getClass().getDeclaredAnnotation(ErrorMessage.class);
		if (errorMessage == null) {
			logger.warn(String.format("Exception of type [%s] has been thrown without ErrorMessage annotation",
					ex.getClass().getSimpleName()));
			return new ResponseEntity<Object>(Response.error(500, createUserMessage(ex, ex.getMessage()),
					createDeveloperMessage(ex, ex.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<Object>(
					Response.error(errorMessage.code(), createUserMessage(ex, errorMessage.message()),
							createDeveloperMessage(ex, errorMessage.developerMessage())),
					errorMessage.status());
		}
	}

	private String injectParametersToMessage(String message, ExceptionBase ex) {

		Pattern r = Pattern.compile("(\\{(.*?)\\})");
		Matcher matcher = r.matcher(message);
		while (matcher.find()) {
			Object param = ex.getParameter(matcher.group(2));
			message = message.replace(matcher.group(1), param == null ? "--" : param.toString());
		}
		return message;
	}

	private String createUserMessage(ExceptionBase ex, String userMessage) {
		if (userMessage.startsWith("${")) {
			userMessage = localizationService.localizedText(userMessage.substring(2, userMessage.length() - 1));
		}
		// if message not provided by annotation
		if (StringUtils.isEmpty(userMessage)) {
			// eg errors.invalidtoken
			userMessage = "errors." + ex.getClass().getSimpleName().toLowerCase().replace("exception", "");
			userMessage = this.localizationService.localizedText(userMessage);
		}
		return injectParametersToMessage(userMessage, ex);
	}

	private String createDeveloperMessage(ExceptionBase ex, String developerMessage) {
		// String developerMessage = errorMessage.developerMessage();
		// if developerMessage not provided by annotation
		if (developerMessage.startsWith("${")) {
			developerMessage = localizationService
					.localizedText(developerMessage.substring(2, developerMessage.length() - 1));
		}
		if (StringUtils.isEmpty(developerMessage)) {
			// eg errors.invalidtoken.dev
			developerMessage = "errors." + ex.getClass().getSimpleName().toLowerCase().replace("exception", "")
					+ ".dev";
			developerMessage = this.localizationService.localizedText(developerMessage);
		}

		return injectParametersToMessage(developerMessage, ex);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if (ex instanceof MethodArgumentNotValidException) {
			BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
			List<FieldError> bindingErrors = bindingResult.getFieldErrors();
			List<ObjectError> allErrors = bindingResult.getAllErrors();
			List<String> errors = bindingErrors.stream().map(
					bindingError -> String.format("%s %s", bindingError.getField(), bindingError.getDefaultMessage()))
					.collect(Collectors.toList());
			errors.addAll(allErrors.stream()
					.map(error -> String.format("%s %s", error.getObjectName(), error.getDefaultMessage()))
					.collect(Collectors.toList()));

			Response response = Response.error(400, localizationService.localizedError("badrequest"),
					localizationService.localizedError("badrequest.dev"));
			for (String err : errors) {
				response.addError(err);
			}
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		if (ex instanceof MethodArgumentTypeMismatchException) {
			Response response = Response.error(400, localizationService.localizedError("badrequest"),
					localizationService.localizedError("badrequest.dev"));
			response.addError(String.format("Parameter [%s] is not valid for type [%s]",
					((MethodArgumentTypeMismatchException) ex).getName(),
					((MethodArgumentTypeMismatchException) ex).getRequiredType().getSimpleName()));
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		if (ex instanceof MissingServletRequestParameterException) {
			Response response = Response.error(400, localizationService.localizedError("badrequest"),
					localizationService.localizedError("badrequest.dev"));
			response.addError(String.format("Parameter [%s] of type [%s] is not provided",
					((MissingServletRequestParameterException) ex).getParameterName(),
					((MissingServletRequestParameterException) ex).getParameterType()));
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		logger.warn("Can not bind data", ex);
		Response response = Response.error(400, this.localizationService.localizedError("malformedjson"),
				this.localizationService.localizedError("malformedjson.dev"));
		if (ex.getCause() instanceof JsonMappingException) {
			response.addError("Body is not correct json");
		}
		if (ex.getCause() instanceof JsonParseException) {
			response.addError("Body is not correct json");
		}
		if (ex.getCause() instanceof InvalidFormatException) {
			InvalidFormatException invalidFormatException = (InvalidFormatException) ex.getCause();
			String path = String.join(".",
					invalidFormatException.getPath().stream().map(x -> x.getFieldName()).collect(Collectors.toList()));
			if (invalidFormatException.getTargetType().isEnum()) {
				String enumValues = String.join(",",
						Arrays.asList(((Class<Enum>) invalidFormatException.getTargetType()).getEnumConstants())
								.stream().map(x -> x.name()).collect(Collectors.toList()));
				response.addError(String.format("Value [%s] must be one of [%s]", path, enumValues));
			}
			if (invalidFormatException.getTargetType().equals(Integer.class)
					|| invalidFormatException.getTargetType().equals(Long.class)
					|| invalidFormatException.getTargetType().equals(Double.class)
					|| invalidFormatException.getTargetType().equals(Float.class)
					|| invalidFormatException.getTargetType().equals(Byte.class)) {
				response.addError(String.format("Invalid value for [%s] of type number", path,
						invalidFormatException.getTargetType().getSimpleName()));
			}
			if (invalidFormatException.getTargetType().isArray()
					|| invalidFormatException.getTargetType().equals(Iterable.class)
					|| invalidFormatException.getTargetType().equals(Collection.class)) {
				response.addError(String.format("Invalid value for [%s] of type array", path,
						invalidFormatException.getTargetType().getSimpleName()));
			}
		}
		if (ex.getCause() == null) {
			response.addError("Body is empty");
		}
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

}
