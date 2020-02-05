package tech.builtrix.base;

import org.springframework.http.HttpStatus;

import java.lang.annotation.*;

/**
 * @author : Sahar
 * @date : 20/10/2019
 * @project : metric
 * @class : ErrorMessage
 */

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ErrorMessage {
	HttpStatus status() default HttpStatus.BAD_REQUEST;

	int code() default 400;

	String message() default "";

	String developerMessage() default "";
}
