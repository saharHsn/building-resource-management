package tech.builtrix.exception;

import org.springframework.http.HttpStatus;
import tech.builtrix.base.ErrorMessage;

@ErrorMessage(code = 400011, status = HttpStatus.BAD_REQUEST)
public class InvalidEmailException extends ExceptionBase {
}
