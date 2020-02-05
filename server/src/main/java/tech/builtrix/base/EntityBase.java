package tech.builtrix.base;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@DynamoDBTable(tableName = "EntityBase")
public abstract class EntityBase<T extends EntityBase<T>> implements Serializable {
	@DynamoDBHashKey
	@DynamoDBAutoGeneratedKey
	private String id;

	@DynamoDBAttribute
	private Date creationTime;

	@DynamoDBAttribute
	private Date updateTime;

	@DynamoDBAttribute
	private String correlationId;

	@DynamoDBAttribute
	private Boolean active = true;

	/**
	 * Entities compare by identity, not by attributes.
	 *
	 * @param that The other entity of the same type
	 * @return true if the identities are the same, regardless of the other
	 *         attributes.
	 * @throws IllegalStateException one of the entities does not have the identity
	 *                               attribute set.
	 */
	public boolean sameIdentityAs(final T that) {
		return this.equals(that);
	}

	// @PrePersist
	protected void onCreate() {

	}

	// @PreUpdate
	protected void onUpdate() {
	}

	@Override
	public boolean equals(final Object object) {
		if (!(object instanceof EntityBase)) {
			return false;
		}
		final EntityBase<?> that = (EntityBase<?>) object;
		_checkIdentity(this);
		_checkIdentity(that);
		return this.id.equals(that.getId());
	}

	/**
	 * Checks the passed entity, if it has an identity. It gets an identity only by
	 * saving.
	 *
	 * @throws IllegalStateException the passed entity does not have the identity
	 *                               attribute set.
	 */
	private void _checkIdentity(final EntityBase<?> entity) {
		if (entity.getId() == null) {
			throw new IllegalStateException("Identity missing in entity: " + entity);
		}
	}

	@Override
	public String toString() {
		return String.format("%s[id=%s]", this.getClass().getSimpleName(), this.id);

	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : 0;
	}
}
