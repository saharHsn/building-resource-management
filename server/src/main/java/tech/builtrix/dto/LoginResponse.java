package tech.builtrix.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Created By sahar at 10/17/19
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class LoginResponse {
    private UserDto user;

    public LoginResponse(UserDto user) {
        this.user = user;
    }
}
