package tech.builtrix.models.user;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.builtrix.base.EntityBase;
import tech.builtrix.models.EnumConverter;
import tech.builtrix.models.user.enums.TokenPurpose;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created By Sahar at 2/23/19 : 10:43 AM
 **/

@DynamoDBTable(tableName = "Verification_Token")
@Setter
@Getter
@NoArgsConstructor
public class VerificationToken extends EntityBase<VerificationToken> {
	private static final int EXPIRATION = 60 * 24;

	@DynamoDBAttribute
	private String token;
	@DynamoDBAttribute
	private Date expiryDate;
	@DynamoDBTypeConverted(converter = EnumConverter.class)
	@DynamoDBAttribute(attributeName = "TokenPurpose")
	private TokenPurpose purpose;
	@DynamoDBAttribute(attributeName = "user_id")
	private String user;
	@DynamoDBAttribute
	private Date usedTime;

	public VerificationToken(String token, User user) {
		this.token = token;
		this.user = user.getId();
	}

	private Date calculateExpiryDate(int expiryTimeInMinutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));
		cal.add(Calendar.MINUTE, expiryTimeInMinutes);
		return new Date(cal.getTime().getTime());
	}

	public void updateToken(final String token) {
		this.token = token;
		this.expiryDate = calculateExpiryDate(EXPIRATION);
	}

	//

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expiryDate == null) ? 0 : expiryDate.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final VerificationToken other = (VerificationToken) obj;
		if (expiryDate == null) {
			if (other.expiryDate != null) {
				return false;
			}
		} else if (!expiryDate.equals(other.expiryDate)) {
			return false;
		}
		if (token == null) {
			if (other.token != null) {
				return false;
			}
		} else if (!token.equals(other.token)) {
			return false;
		}
		if (user == null) {
			return other.user == null;
		} else
			return user.equals(other.user);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Token [String=").append(token).append("]").append("[Expires").append(expiryDate).append("]");
		return builder.toString();
	}

	/*
	 * public boolean isExpired() { return this.getExpiryDate().before(new Date());
	 * }
	 */
}
