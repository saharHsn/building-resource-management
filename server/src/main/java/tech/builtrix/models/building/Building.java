package tech.builtrix.models.building;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.builtrix.base.EntityBase;
import tech.builtrix.web.dtos.bill.BuildingDto;

/**
 * Created By sahar-hoseini at 20. Jun 2019 4:03 PM
 **/
@DynamoDBTable(tableName = "Building")
@Setter
@Getter
@NoArgsConstructor
public class Building extends EntityBase<Building> {
	@DynamoDBAttribute
	private String name;
	@DynamoDBAttribute
	private String postalCode;
	@DynamoDBAttribute
	private String postalAddress;
	/*
	 * @DynamoDBAttribute(attributeName = "FullAddress") private String fullAddress;
	 */
	@DynamoDBTypeConverted(converter = BuildingUsageConverter.class)
	@DynamoDBAttribute(attributeName = "BuildingUsage")
	private BuildingUsage usage = BuildingUsage.UNKNOWN;
	@DynamoDBTypeConverted(converter = EnergyCertificateConverter.class)
	@DynamoDBAttribute(attributeName = "EnergyCertificate")
	private EnergyCertificate energyCertificate = EnergyCertificate.Others;
	@DynamoDBTypeConverted(converter = BuildingAgeConverter.class)
	@DynamoDBAttribute(attributeName = "BuildingAge")
	private BuildingAge age = BuildingAge.UNKNOWN;
	@DynamoDBAttribute
	private float area;
	@DynamoDBAttribute
	private Integer numberOfPeople;
	// @DynamoDBAttribute
	// private Map<Date, Integer> numOfPeopleMap;
	@DynamoDBAttribute(attributeName = "Owner")
	private String owner;

	public Building(BuildingDto buildingDto) {
		this.name = buildingDto.getName();
		this.postalAddress = buildingDto.getPostalAddress();
		this.postalCode = buildingDto.getPostalCode();
		this.usage = buildingDto.getUsage();
		this.energyCertificate = buildingDto.getEnergyCertificate();
		this.age = buildingDto.getAge();
		this.area = buildingDto.getArea();
		this.numberOfPeople = buildingDto.getNumberOfPeople();
		this.owner = buildingDto.getOwner().getId();
		// this.numOfPeopleMap = buildingDto.getNumOfPeopleMap();
	}
}
