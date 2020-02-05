package tech.builtrix.exceptions;

import org.springframework.http.HttpStatus;
import tech.builtrix.base.ErrorMessage;

/**
 * Created By sahar-hoseini at 13. Jun 2019 3:50 PM
 **/

@ErrorMessage(code = 400011, status = HttpStatus.BAD_REQUEST)
public class AlreadyExistException extends ExceptionBase {
	public AlreadyExistException(String field, String value) {
		addParameter("field", field).addParameter("value", value);
	}

	public AlreadyExistException(String message) {
		super(message);
	}
}