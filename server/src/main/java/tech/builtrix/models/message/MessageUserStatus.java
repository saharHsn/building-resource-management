package tech.builtrix.models.message;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Getter;
import lombok.Setter;

@DynamoDBTable(tableName = "Message_User_Status")
@Setter
@Getter
public class MessageUserStatus {
    @DynamoDBAttribute
    private String user;
    @DynamoDBAttribute
    private boolean read = false;
}
