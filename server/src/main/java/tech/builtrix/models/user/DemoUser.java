package tech.builtrix.models.user;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Getter;
import lombok.Setter;
import tech.builtrix.base.EntityBase;

@DynamoDBTable(tableName = "Demo_User")
@Setter
@Getter
public class DemoUser extends EntityBase<DemoUser> {
    @DynamoDBAttribute
    private String fullName;
    @DynamoDBAttribute
    private String emailAddress;
    @DynamoDBAttribute
    private String jobTitle;
    @DynamoDBAttribute
    private String interest;
    @DynamoDBAttribute
    private boolean subscribe;
}
