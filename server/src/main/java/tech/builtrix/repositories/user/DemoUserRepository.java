package tech.builtrix.repositories.user;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import tech.builtrix.base.RepositoryBase;
import tech.builtrix.models.user.DemoUser;

import java.util.List;

@EnableScan
public interface DemoUserRepository extends RepositoryBase<DemoUser> {
    List<DemoUser> findByEmailAddress(String email);
}
