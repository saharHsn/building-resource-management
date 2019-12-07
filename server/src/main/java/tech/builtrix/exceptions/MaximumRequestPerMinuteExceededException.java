package tech.builtrix.exceptions;

import org.springframework.http.HttpStatus;
import tech.builtrix.base.ErrorMessage;

@ErrorMessage(code = 400029, status = HttpStatus.TOO_MANY_REQUESTS)
public class MaximumRequestPerMinuteExceededException extends ExceptionBase {

}
