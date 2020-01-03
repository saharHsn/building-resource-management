package tech.builtrix.web.dtos.emailToken;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.builtrix.validations.PasswordMatches;
import tech.builtrix.validations.ValidEmail;
import tech.builtrix.validations.ValidPassword;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@JsonInclude
@NoArgsConstructor
@PasswordMatches
public class RegisterUserDto {
    @NotNull
    @Size(min = 1, message = "{Size.userDto.firstName}")
    private String firstName;
    @NotNull
    @Size(min = 1, message = "{Size.userDto.lastName}")
    private String lastName;

    @NotNull
    @ValidEmail
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

