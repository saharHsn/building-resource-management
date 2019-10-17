package tech.builtrix.model.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.builtrix.base.EntityBase;

import javax.persistence.*;

@Table(name = "user_login")
@Entity
@Setter(value = AccessLevel.PACKAGE)
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class UserLogin extends EntityBase<UserLogin> {
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    private User user;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "message")
    private String message;
    @Column(name = "provider")
    private String provider;
    @Column(name = "provider_response")
    private String providerResponse;
}
