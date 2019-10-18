package tech.builtrix.repository.user;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import tech.builtrix.base.RepositoryBase;
import tech.builtrix.model.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
public class UserRepository implements RepositoryBase<User> {

    private final DynamoDBMapper mapper;

    public UserRepository(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public void insert(User user) {
        mapper.save(user);
    }

    public User get(String userId, String lastName) {
        return mapper.load(User.class, userId, lastName);
    }

    public User findByEmail(String email) {
        return mapper.load(User.class, email);
    }

    public void update(User user) {
        try {
            mapper.save(user, buildDynamoDBSaveExpression(user));
        } catch (ConditionalCheckFailedException exception) {
            logger.error("invalid data - " + exception.getMessage());
        }
    }


    private DynamoDBSaveExpression buildDynamoDBSaveExpression(User user) {
        DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression();
        Map<String, ExpectedAttributeValue> expected = new HashMap<>();
        expected.put("userId", new ExpectedAttributeValue(new AttributeValue(user.getId()))
                .withComparisonOperator(ComparisonOperator.EQ));
        saveExpression.setExpected(expected);
        return saveExpression;
    }

    public List<User> getAll() {
        return null;
    }

    //--------------------------------------------------------------------------------------------------------------\\

    @Override
    public <S extends User> S save(S entity) {
        try {
            mapper.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public <S extends User> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<User> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public Iterable<User> findAll() {
        return null;
    }

    @Override
    public Iterable<User> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    public void delete(User user) {
        mapper.delete(user);
    }

    @Override
    public void deleteAll(Iterable<? extends User> entities) {

    }

    @Override
    public void deleteAll() {

    }


}
