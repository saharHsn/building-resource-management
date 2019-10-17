package tech.builtrix.limit;

import org.springframework.http.HttpStatus;
import tech.builtrix.base.ErrorMessage;
import tech.builtrix.exception.ExceptionBase;

@ErrorMessage(code = 400029, status = HttpStatus.TOO_MANY_REQUESTS)
public class MaximumRequestPerMinuteExceededException extends ExceptionBase {

}
