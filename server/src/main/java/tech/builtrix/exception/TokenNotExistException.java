package tech.builtrix.exception;

import org.springframework.http.HttpStatus;
import tech.builtrix.base.ErrorMessage;

/**
 * Created By sahar-hoseini at 13. Jun 2019 4:30 PM
 **/
@ErrorMessage(code = 401, status = HttpStatus.UNAUTHORIZED)
public class TokenNotExistException extends ExceptionBase {
}