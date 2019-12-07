/*
package tech.builtrix.model.contact;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.builtrix.dto.AddressDto;

*/
/**
 * Created By sahar-hoseini at 20. Jun 2019 4:05 PM
 **//*

@Setter
@Getter
@NoArgsConstructor
@DynamoDBDocument
@DynamoDBTable(tableName = "Address")
public class Address  {
    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String id;
    @DynamoDBAttribute
    private String postalAddress;
    @DynamoDBAttribute
    protected String postalCode;

    public Address() {

    }

    public Address(String postalAddress, String postalCode) {
        this.postalCode = postalCode;
        this.postalAddress = postalAddress;
    }
}
*/