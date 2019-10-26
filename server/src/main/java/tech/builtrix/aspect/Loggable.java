package tech.builtrix.aspect;

import org.springframework.boot.logging.LogLevel;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Loggable {
    LogLevel level() default LogLevel.DEBUG;

    String prefixMessage() default "";
}


