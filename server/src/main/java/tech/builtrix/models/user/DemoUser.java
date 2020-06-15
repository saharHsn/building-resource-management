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
    private String firstName;
    @DynamoDBAttribute
    private String lastName;
    @DynamoDBAttribute
    private String emailAddress;
}
