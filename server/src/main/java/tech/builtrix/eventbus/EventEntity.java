package tech.builtrix.eventbus;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.builtrix.base.EntityBase;

@Data
@DynamoDBTable(tableName = "InternalMessages")
@NoArgsConstructor
@Getter
@Setter
public class EventEntity extends EntityBase {
    @DynamoDBAttribute(attributeName = "message")
    private String msg;
    @DynamoDBAttribute(attributeName = "isSent")
    private Boolean sent;
    @DynamoDBAttribute(attributeName = "type")
    private String type;
}