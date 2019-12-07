package tech.builtrix.dtos.emailToken;

import lombok.Data;

@Data
public class TokenResponse {
    private String code;

    public TokenResponse(String code) {
        this.code = code;
    }
}
