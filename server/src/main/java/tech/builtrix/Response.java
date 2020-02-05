package tech.builtrix;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tech.builtrix.commands.CommandBase;
import tech.builtrix.context.RequestContext;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Response<TContent> {

	private long timeStamp;

	private String path;

	private Integer status;

	private String requestId;

	private String message;

	private TContent content;

	private List<CommandBase> commands = new ArrayList<>();

	private String errorMessage;

	private String developerMessage;

	private List<String> errors;

	private Long duration;

	protected Response(int status, String message, TContent content) {
		// this.requestId = ((RequestContext)
		// RequestContextHolder.currentRequestAttributes().getAttribute("requestContext",
		// 0)).getCorrelationId();
		this.path = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
				.getRequestURI();
		this.status = status;
		this.message = message;
		this.content = content;
		this.timeStamp = new Date().getTime();
		this.duration = Calendar.getInstance().getTimeInMillis()
				- ((RequestContext) RequestContextHolder.currentRequestAttributes().getAttribute("requestContext", 0))
						.getRequestTime();
	}

	public static Response<Void> ok() {
		return ok(null);
	}

	public static <TData> Response<TData> ok(TData data) {
		return new Response(0, "OK", data);
	}

	public static <TData> Response<TData> error(int code, String message, String... errors) {
		Response response = new Response(code, null, null);
		response.errorMessage = message;
		if (response.errors == null) {
			for (String error : errors) {
				response.errors.add(error);
			}
		}
		return response;
	}

	public static <TData> Response<TData> error(int code, String message) {
		return new Response(code, message, null);
	}

	public static <TData> Response<TData> error(int code, String message, String developerMessage) {
		return new Response(code, message, null) {
			{
				setDeveloperMessage(developerMessage);
			}
		};
	}

	public static <TData> Response<TData> unauthorized(String message) {
		return error(401, message);
	}

	public static Response<String> entityNotFound(String message) {
		return error(404, "Entity not found", message);
	}

	public List<CommandBase> getCommands() {
		return this.commands;
	}

	public Response<TContent> addCommands(CommandBase... commands) {
		for (CommandBase command : commands)
			this.commands.add(command);
		return this;
	}

	public Response<TContent> addCommand(CommandBase command) {
		this.commands.add(command);
		return this;
	}

	public Response<TContent> setMessage(String message) {
		this.message = message;
		return this;
	}

	public Response<TContent> setErrorMessage(String message) {
		this.errorMessage = message;
		return this;
	}

	public Response<TContent> addError(String error) {
		if (this.errors == null)
			this.errors = new ArrayList<>();

		this.errors.add(error);
		return this;
	}

	public Response<TContent> addDeveloperMessage(String message) {
		this.developerMessage = message;
		return this;
	}

}