package tech.builtrix.models.user;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.builtrix.base.EntityBase;
import tech.builtrix.models.EnumConverter;

import java.util.Date;

/**
 * Created By Sahar at 2/23/19 : 10:43 AM
 **/

@DynamoDBTable(tableName = "User_Token")
@Setter
@Getter
@NoArgsConstructor
public class UserToken extends EntityBase<UserToken> {
    @DynamoDBAttribute
    private String token;
    @DynamoDBAttribute
    private Date expirationTime;
    @DynamoDBTypeConverted(converter = EnumConverter.class)
    @DynamoDBAttribute(attributeName = "TokenPurpose")
    private TokenPurpose purpose;
    @DynamoDBAttribute(attributeName = "user_id")
    private String user;
    @DynamoDBAttribute
    private Date usedTime;

    public boolean isExpired() {
        return this.getExpirationTime().before(new Date());
    }
}

