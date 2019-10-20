package tech.builtrix.repository.user;


import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import tech.builtrix.base.RepositoryBase;
import tech.builtrix.model.user.User;

import java.util.List;

@EnableScan
public interface UserRepository extends RepositoryBase<User> {
    List<User> findByEmailAddress(String email);
}
