package tech.builtrix.exceptions.session;

import org.springframework.http.HttpStatus;
import tech.builtrix.base.ErrorMessage;
import tech.builtrix.exceptions.ExceptionBase;

@ErrorMessage(code = 401002, status = HttpStatus.UNAUTHORIZED)
public class EmptySessionException extends ExceptionBase {
}

