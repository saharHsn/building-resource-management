package tech.builtrix.repository.user;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.builtrix.base.RepositoryBase;
import tech.builtrix.model.user.UserToken;

/**
 * Created By Sahar at 2/23/19 : 10:43 AM
 **/

@Repository
public
interface UserTokenRepository extends RepositoryBase<UserToken> {

    //@Query("SELECT t FROM UserToken t WHERE t.usedTime IS NULL AND t.value = :value")
    UserToken findByValue(@Param("value") String value);


    //@Query("SELECT t FROM UserToken t WHERE t.user.id = :userId AND t.value = :token ")
    UserToken findByUserIdAndToken(String userId, String token);


}
