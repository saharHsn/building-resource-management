package tech.builtrix.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class EnumConverter<T extends Enum<T>> implements DynamoDBTypeConverter<String, T> {
    private final Class<T> sourceType;

    public EnumConverter(Class<T> sourceType) {
        this.sourceType = sourceType;
    }


    @Override
    public String convert(T anEnum) {
        return anEnum.name();
    }

    @Override
    public T unconvert(String s) {
        return Enum.valueOf(sourceType, s);
    }
}
