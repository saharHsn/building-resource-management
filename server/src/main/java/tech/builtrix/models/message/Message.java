package tech.builtrix.models.message;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Getter;
import lombok.Setter;
import tech.builtrix.base.EntityBase;

import java.util.Map;

@DynamoDBTable(tableName = "Message")
@Setter
@Getter
public class Message extends EntityBase<Message> {
    @DynamoDBAttribute
    private String body;
    @DynamoDBAttribute
    private String ownerBuilding;
    @DynamoDBAttribute
    private Map<String, Boolean> usersStatus;
}
