package tech.builtrix.exceptions;

import org.springframework.http.HttpStatus;
import tech.builtrix.base.ErrorMessage;

@ErrorMessage(code = 401, status = HttpStatus.UNAUTHORIZED)
public class TokenExpiredException extends ExceptionBase {
    public TokenExpiredException(String message) {
        super(message);
    }
}
