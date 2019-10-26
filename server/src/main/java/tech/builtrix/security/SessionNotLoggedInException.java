package tech.builtrix.security;

import org.springframework.http.HttpStatus;
import tech.builtrix.base.ErrorMessage;
import tech.builtrix.exception.ExceptionBase;

@ErrorMessage(code = 401004, status = HttpStatus.FORBIDDEN)
public class SessionNotLoggedInException extends ExceptionBase {

}
