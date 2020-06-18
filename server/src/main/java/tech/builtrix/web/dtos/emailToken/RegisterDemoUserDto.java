package tech.builtrix.web.dtos.emailToken;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.builtrix.validations.ValidEmail;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@JsonInclude
@NoArgsConstructor
public class RegisterDemoUserDto {
    @NotNull
    @Size(min = 1, message = "{Size.userDto.firstName}")
    private String fullName;
    @NotNull
    @ValidEmail
    private String emailAddress;
    @NotNull
    private String jobTitle;
    private String interest;
    private boolean subscribe;
}
