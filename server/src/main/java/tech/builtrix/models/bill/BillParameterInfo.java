package tech.builtrix.models.bill;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import tech.builtrix.base.EntityBase;
import tech.builtrix.web.dtos.bill.BillParameterDto;

import java.util.Date;

/**
 * Created By sahar at 11/30/19
 */

@Setter
@Getter
@AllArgsConstructor
@DynamoDBTable(tableName = "Bill_Parameter_Info")
public class BillParameterInfo extends EntityBase<BillParameterInfo> {
    /*5 columns (initial date, end date, consumption, tariff price, total cost of each tariff);*/
    @DynamoDBAttribute
    private Date initialDate;
    @DynamoDBAttribute
    private Date endDate;
    @DynamoDBAttribute
    private Float consumption;
    @DynamoDBAttribute
    private Float cost;
    @DynamoDBAttribute
    private Float tariffPrice;
    @DynamoDBAttribute
    private Float totalTariffCost;

    public BillParameterInfo() {
    }

    public BillParameterInfo(BillParameterDto billParameterDto) {
        this.initialDate = billParameterDto.getInitialDate();
        this.endDate = billParameterDto.getEndDate();
        this.cost = billParameterDto.getCost();
        this.consumption = billParameterDto.getConsumption();
        this.tariffPrice = billParameterDto.getTariffPrice();
        this.totalTariffCost = billParameterDto.getTotalTariffCost();
    }

    /*initial date, end date, consumption, tariff price, total cost of each tariff*/

}
