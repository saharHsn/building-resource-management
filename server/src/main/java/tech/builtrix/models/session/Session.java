package tech.builtrix.models.session;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.builtrix.base.EntityBase;
import tech.builtrix.models.user.User;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@DynamoDBTable(tableName = "Session")
@Setter
@Getter
@NoArgsConstructor
public class Session extends EntityBase<Session> {

    @Column(name = "session_key", nullable = false, unique = true, updatable = false)
    private String sessionKey;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    /* @JoinColumn(name = "device_id", nullable = false)
     @ManyToOne(fetch = FetchType.LAZY)
     private Device device;
 */
    @Column(name = "exp_date")
    private Date expirationDate;

    @Column(name = "last_request_date")
    private Date lastRequestDate;

    @Column(name = "request_count")
    private Integer requestCount;

}


