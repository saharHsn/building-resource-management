package tech.builtrix.security.session;

import org.springframework.http.HttpStatus;
import tech.builtrix.base.ErrorMessage;
import tech.builtrix.exception.ExceptionBase;

/**
 * @author : pc`
 * @date : 14/08/2018
 */
@ErrorMessage(status = HttpStatus.UNAUTHORIZED, code = 4001)
public class SessionExpiredException extends ExceptionBase {
}
