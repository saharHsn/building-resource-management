package tech.builtrix.model.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.builtrix.base.EntityBase;

import javax.persistence.*;
import java.util.Date;

/**
 * Created By Sahar at 2/23/19 : 10:43 AM
 **/

@Table(name = "users_token")
@Entity
@Setter
@Getter
@NoArgsConstructor
public class UserToken extends EntityBase<UserToken> {

    @Column(name = "value", length = 80)
    private String value;
    @Column(name = "exp_time", length = 80)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationTime;
    @Enumerated(EnumType.STRING)
    @Column(name = "purpose", length = 16)
    private TokenPurpose purpose;
    @JoinColumn(name = "user_id")
    @ManyToOne(cascade = {CascadeType.MERGE}, optional = false)
    private User user;
    @Column(name = "used_time")
    private Date usedTime;

    public boolean isExpired() {
        return this.getExpirationTime().before(new Date());
    }
}

