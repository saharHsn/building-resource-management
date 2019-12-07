package tech.builtrix.exceptions.session;

import org.springframework.http.HttpStatus;
import tech.builtrix.base.ErrorMessage;
import tech.builtrix.exceptions.ExceptionBase;

@ErrorMessage(code = 401001, status = HttpStatus.UNAUTHORIZED)
public class InvalidSessionException extends ExceptionBase {
    public InvalidSessionException() {

    }

    public InvalidSessionException(Throwable inner) {
        initCause(inner);
    }
}
