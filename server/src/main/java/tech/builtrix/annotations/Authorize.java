package tech.builtrix.annotations;


import tech.builtrix.models.user.Role;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {
    Role[] roles() default {};
}
