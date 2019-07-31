package tech.builtrix.repository.building;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import tech.builtrix.base.RepositoryBase;
import tech.builtrix.model.building.Building;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
public class BuildingRepository implements RepositoryBase<Building> {

    private final DynamoDBMapper mapper;

    public BuildingRepository(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    public Building get(String id) {
        return mapper.load(Building.class, id);
    }

    public void update(Building building) {
        try {
            mapper.save(building, buildDynamoDBSaveExpression(building));
        } catch (ConditionalCheckFailedException exception) {
            logger.error("invalid data - " + exception.getMessage());
        }
    }


    public void delete(Building building) {
        mapper.delete(building);
    }

    private DynamoDBSaveExpression buildDynamoDBSaveExpression(Building building) {
        DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression();
        Map<String, ExpectedAttributeValue> expected = new HashMap<>();
        expected.put("buildingId", new ExpectedAttributeValue(new AttributeValue(building.getId()))
                .withComparisonOperator(ComparisonOperator.EQ));
        saveExpression.setExpected(expected);
        return saveExpression;
    }

    //-----------------------------------------------------------------------------------------------------------------\\

    @Override
    public void deleteAll(Iterable<? extends Building> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Building> S save(S entity) {
        mapper.save(entity);
        return entity;
    }

    @Override
    public <S extends Building> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Building> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    public List<Building> findAll() {
        return null;
    }

    @Override
    public Iterable<Building> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

}
