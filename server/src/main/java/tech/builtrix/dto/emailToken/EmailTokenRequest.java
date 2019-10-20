package tech.builtrix.dto.emailToken;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EmailTokenRequest {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    @NotNull
    private String emailAddress;
    @NotNull
    private String password;
    @NotNull
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

