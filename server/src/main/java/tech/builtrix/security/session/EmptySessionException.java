package tech.builtrix.security.session;

import org.springframework.http.HttpStatus;
import tech.builtrix.base.ErrorMessage;
import tech.builtrix.exception.ExceptionBase;

@ErrorMessage(code = 401002, status = HttpStatus.UNAUTHORIZED)
public class EmptySessionException extends ExceptionBase {
}

