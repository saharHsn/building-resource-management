package tech.builtrix.models.bill.enums;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class ParameterTypeConverter implements DynamoDBTypeConverter<String, ParameterType> {

    @Override
    public String convert(ParameterType parameterType) {
        return parameterType.name();
    }

    @Override
    public ParameterType unconvert(String s) {
        return ParameterType.valueOf(s);
    }

}
