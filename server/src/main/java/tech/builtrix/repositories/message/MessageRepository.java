package tech.builtrix.repositories.message;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import tech.builtrix.base.RepositoryBase;
import tech.builtrix.models.message.Message;

import java.util.List;

@EnableScan
public interface MessageRepository extends RepositoryBase<Message> {
    List<Message> findByOwnerBuilding(String buildingId);
}
