package tech.builtrix.repositories.user;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.builtrix.base.RepositoryBase;
import tech.builtrix.models.user.User;
import tech.builtrix.models.user.UserToken;

import java.util.Date;
import java.util.stream.Stream;

/**
 * Created By Sahar at 2/23/19 : 10:43 AM
 **/

@Repository
public interface UserTokenRepository extends RepositoryBase<UserToken> {

    UserToken findByToken(@Param("value") String value);

    UserToken findByUser(User user);

    Stream<UserToken> findAllByExpirationTimeLessThan(Date now);

    void deleteByExpirationTimeLessThan(Date now);
}
