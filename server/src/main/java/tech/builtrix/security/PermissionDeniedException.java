package tech.builtrix.security;

import com.google.common.collect.Lists;
import org.springframework.http.HttpStatus;
import tech.builtrix.base.ErrorMessage;
import tech.builtrix.exception.ExceptionBase;
import tech.builtrix.model.user.Role;

import java.util.List;
import java.util.stream.Collectors;

@ErrorMessage(code = 401005, status = HttpStatus.FORBIDDEN)
public class PermissionDeniedException extends ExceptionBase {
    public PermissionDeniedException(Role... roles) {
        List<String> rolesString = Lists.newArrayList(roles).stream().map(x -> x.toString()).collect(Collectors.toList());
        addParameter("roles", String.join(",", rolesString));
    }
}
