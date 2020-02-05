package tech.builtrix.models.session;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.builtrix.base.EntityBase;

import java.util.Date;

@DynamoDBTable(tableName = "Session")
@Setter
@Getter
@NoArgsConstructor
public class Session extends EntityBase<Session> {
	@DynamoDBAttribute
	private String sessionKey;
	@DynamoDBAttribute
	private String user;
	@DynamoDBAttribute
	private Date expirationDate;
	@DynamoDBAttribute
	private Date lastRequestDate;
	@DynamoDBAttribute
	private Integer requestCount;
}
