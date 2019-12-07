package tech.builtrix.exceptions;


import org.springframework.http.HttpStatus;
import tech.builtrix.base.ErrorMessage;

/**
 * @author : pc`
 * @date : 04/04/2018
 */

@ErrorMessage(code = 400011, status = HttpStatus.BAD_REQUEST)
public class InvalidPasswordException extends ExceptionBase {
}
