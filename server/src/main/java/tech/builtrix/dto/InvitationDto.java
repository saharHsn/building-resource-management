package tech.builtrix.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Created By sahar at 10/23/19
 */

@JsonInclude
@Data
@NoArgsConstructor
public class InvitationDto {

    @NotNull
    private String inviteeEmail;

    private String subject;
    private String message;
}
