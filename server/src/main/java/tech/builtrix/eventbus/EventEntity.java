package tech.builtrix.eventbus;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.builtrix.base.EntityBase;

@Data
@DynamoDBTable(tableName = "Internal_Messages")
@NoArgsConstructor
@Getter
@Setter
public class EventEntity extends EntityBase {
	@DynamoDBAttribute(attributeName = "message")
	private String msg;
	@DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.BOOL)
	@DynamoDBAttribute(attributeName = "isSent")
	private boolean sent;
	@DynamoDBAttribute(attributeName = "type")
	private String type;
}