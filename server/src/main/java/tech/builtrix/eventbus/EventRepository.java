package tech.builtrix.eventbus;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface EventRepository extends CrudRepository<EventEntity, Long> {
    List<EventEntity> findAllBySentIsFalse();

    List<EventEntity> getAllByType(String type);
}


