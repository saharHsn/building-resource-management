package tech.builtrix.models.building.enums;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class EnergyCertificateConverter implements DynamoDBTypeConverter<String, EnergyCertificate> {

	@Override
	public String convert(EnergyCertificate energyCertificate) {
		return energyCertificate.name();
	}

	@Override
	public EnergyCertificate unconvert(String s) {
		return EnergyCertificate.valueOf(s);
	}
}
