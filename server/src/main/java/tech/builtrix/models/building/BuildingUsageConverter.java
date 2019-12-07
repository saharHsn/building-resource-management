package tech.builtrix.models.building;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

/**
 * Created By sahar-hoseini at 11. Jul 2019 5:53 PM
 **/
public class BuildingUsageConverter implements DynamoDBTypeConverter<String, BuildingUsage> {

    @Override
    public String convert(BuildingUsage object) {
        return object.name();
    }

    @Override
    public BuildingUsage unconvert(String s) {
        return BuildingUsage.valueOf(s);
    }
}

          
 