package tech.builtrix.exceptions.session;

import org.springframework.http.HttpStatus;
import tech.builtrix.base.ErrorMessage;
import tech.builtrix.exceptions.ExceptionBase;

/**
 * @author : pc`
 * @date : 14/08/2018
 */

@ErrorMessage(code = 401, status = HttpStatus.UNAUTHORIZED)
public class SessionDisabledException extends ExceptionBase {
}
