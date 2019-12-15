package tech.builtrix.exceptions.session;

import org.springframework.http.HttpStatus;
import tech.builtrix.base.ErrorMessage;
import tech.builtrix.exceptions.ExceptionBase;

/**
 * Created By sahar at 12/15/19
 */
@ErrorMessage(code = 500, status = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends ExceptionBase {
}
