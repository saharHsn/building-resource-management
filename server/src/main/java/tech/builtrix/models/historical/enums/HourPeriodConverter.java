package tech.builtrix.models.historical.enums;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class HourPeriodConverter implements DynamoDBTypeConverter<String, HourPeriod> {

    @Override
    public String convert(HourPeriod object) {
        return object.name();
    }

    @Override
    public HourPeriod unconvert(String s) {
        return HourPeriod.valueOf(s);
    }
}
