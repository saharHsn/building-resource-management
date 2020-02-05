package tech.builtrix.exceptions.session;

import org.springframework.http.HttpStatus;
import tech.builtrix.base.ErrorMessage;
import tech.builtrix.exceptions.ExceptionBase;

@ErrorMessage(code = 401004, status = HttpStatus.FORBIDDEN)
public class SessionNotLoggedInException extends ExceptionBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5192158961372632812L;

}
