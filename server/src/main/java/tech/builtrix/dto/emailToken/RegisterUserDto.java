package tech.builtrix.dto.emailToken;


import lombok.Data;
import tech.builtrix.validation.PasswordMatches;
import tech.builtrix.validation.ValidPassword;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class RegisterUserDto {

    @NotNull
    @Size(min = 1, message = "{Size.userDto.firstName}")
    private String firstName;
    @NotNull
    @Size(min = 1, message = "{Size.userDto.lastName}")
    private String lastName;

    @NotNull
    private String emailAddress;
    @NotNull
    @ValidPassword
    private String password;
    @NotNull
    @Size(min = 1)
    private String confirmPassword;
    //@NotNull
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

