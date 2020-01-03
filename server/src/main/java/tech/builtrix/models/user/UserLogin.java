package tech.builtrix.models.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.builtrix.base.EntityBase;


@Setter(value = AccessLevel.PACKAGE)
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class UserLogin extends EntityBase<UserLogin> {
    private User user;
    private Boolean status;
    private String message;
    private String provider;
    private String providerResponse;
}
