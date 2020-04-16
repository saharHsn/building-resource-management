package tech.builtrix.models.building.enums;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class BuildingAgeConverter implements DynamoDBTypeConverter<String, BuildingAge> {

	@Override
	public String convert(BuildingAge buildingAge) {
		return buildingAge.name();
	}

	@Override
	public BuildingAge unconvert(String s) {
		return BuildingAge.valueOf(s);
	}
}
