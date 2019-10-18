package tech.builtrix.repository.user;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.builtrix.base.RepositoryBase;
import tech.builtrix.model.user.UserToken;

import java.util.Optional;

/**
 * Created By Sahar at 2/23/19 : 10:43 AM
 **/

@Repository
public class UserTokenRepository implements RepositoryBase<UserToken> {

    //@Query("SELECT t FROM UserToken t WHERE t.usedTime IS NULL AND t.value = :value")
    //UserToken findByValue(@Param("value") String value);

    //@Query("SELECT t FROM UserToken t WHERE t.user.id = :userId AND t.value = :token ")
    //UserToken findByUserIdAndToken(String userId, String token);
    private final DynamoDBMapper mapper;

    public UserTokenRepository(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    UserToken findByValue(@Param("value") String value) {
        return mapper.load(UserToken.class, value);
    }

    UserToken findByUserIdAndToken(String userId, String token) {
        return mapper.load(UserToken.class, userId, token);
    }

    @Override
    public <S extends UserToken> S save(S s) {
        return null;
    }

    @Override
    public <S extends UserToken> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<UserToken> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public Iterable<UserToken> findAll() {
        return null;
    }

    @Override
    public Iterable<UserToken> findAllById(Iterable<String> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(UserToken userToken) {

    }

    @Override
    public void deleteAll(Iterable<? extends UserToken> iterable) {

    }

    @Override
    public void deleteAll() {

    }
}
