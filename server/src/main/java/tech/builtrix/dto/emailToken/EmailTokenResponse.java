package tech.builtrix.dto.emailToken;

import lombok.Data;

@Data
public class EmailTokenResponse {
    private String code;

    public EmailTokenResponse(String code) {
        this.code = code;
    }
}
