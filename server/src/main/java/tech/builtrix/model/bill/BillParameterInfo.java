package tech.builtrix.model.bill;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Getter;
import lombok.Setter;
import tech.builtrix.base.EntityBase;

import java.util.Date;

/**
 * Created By sahar at 11/30/19
 */

@Setter
@Getter
@DynamoDBTable(tableName = "BillParameterInfo")
public class BillParameterInfo extends EntityBase<BillParameterInfo> {
    @DynamoDBAttribute
    private Date initialDate;
    @DynamoDBAttribute
    private Date endDate;
    @DynamoDBAttribute
    private Float cost;
    @DynamoDBAttribute
    private Float consumption;
    @DynamoDBAttribute
    private Float tariffPrice;
    @DynamoDBAttribute
    private Float totalTariffCost;

    /*initial date, end date, consumption, tariff price, total cost of each tariff*/

}
