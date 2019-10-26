package tech.builtrix.security.session;

import org.springframework.http.HttpStatus;
import tech.builtrix.base.ErrorMessage;
import tech.builtrix.exception.ExceptionBase;

@ErrorMessage(code = 401001, status = HttpStatus.UNAUTHORIZED)
public class InvalidSessionException extends ExceptionBase {
    public InvalidSessionException() {

    }

    public InvalidSessionException(Throwable inner) {
        initCause(inner);
    }
}
