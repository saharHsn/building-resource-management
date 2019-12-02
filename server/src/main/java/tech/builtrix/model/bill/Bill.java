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
@DynamoDBTable(tableName = "Bill")
public class Bill extends EntityBase<Bill> {
    @DynamoDBAttribute
    private String address;
    @DynamoDBAttribute
    private Date fromDate;
    @DynamoDBAttribute
    private Date toDate;
    //totalPayable Amount;
    @DynamoDBAttribute
    private float totalPayable;
    @DynamoDBAttribute
    private float activeEnergyCost;
    @DynamoDBAttribute
    private float producedCO2;
    //Redes
    @DynamoDBAttribute
    private float powerDemandCost;
    //Average daily consumption of entity in last 12 months
    @DynamoDBAttribute
    private float averageDailyConsumption;

    //BillParameterInfo

    //active power (Energia Ativa) AE --> Energia Ativa
    //Super Vasio
    @DynamoDBAttribute(attributeName = "aEOffHours")
    private String aEOffHours;
    //Vasio Normal
    @DynamoDBAttribute(attributeName = "aEFreeHours")
    private String aEFreeHours;
    //Cheia
    @DynamoDBAttribute(attributeName = "aENormalHours")
    private String aENormalHours;
    //Ponta
    @DynamoDBAttribute(attributeName = "aEPeakHours")
    private String aEPeakHours;

    //cost of network RD --> (Rede)
    //Cost of Power demand in peak hour : Potência Horas de Ponta
    @DynamoDBAttribute(attributeName = "rDPeakHours")
    private String rDPeakHours;
    //cost of contracted power: Potencia Contratada
    @DynamoDBAttribute(attributeName = "rDContractedPower")
    private String rDContractedPower;
    //???? TODO ask Mr.Kamarlouei
    //cost of Energia Reativa: Reativa Fornecida no vazio (Vz)
    @DynamoDBAttribute(attributeName = "rDReactivePower")
    private String rDReactivePower;

}
