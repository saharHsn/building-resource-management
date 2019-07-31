package tech.builtrix.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class EnumConverter implements DynamoDBTypeConverter<String, Enum> {
    @Override
    public String convert(Enum anEnum) {
        return anEnum.name();
    }

    @Override
    public Enum unconvert(String s) {
        return Enum.valueOf(Enum.class, s);
    }
}
