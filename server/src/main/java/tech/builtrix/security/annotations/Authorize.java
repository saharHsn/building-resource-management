package tech.builtrix.security.annotations;


import tech.builtrix.model.user.Role;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {
    Role[] roles() default {};
}
