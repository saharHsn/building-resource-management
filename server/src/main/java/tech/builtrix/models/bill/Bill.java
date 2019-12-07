package tech.builtrix.models.bill;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import tech.builtrix.base.EntityBase;
import tech.builtrix.dtos.bill.BillDto;

import java.util.Date;

/**
 * Created By sahar at 11/30/19
 */


@Setter
@Getter
@AllArgsConstructor
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
    private Float totalPayable;
    @DynamoDBAttribute
    private Float activeEnergyCost;
    @DynamoDBAttribute
    private Float producedCO2;
    //Redes
    @DynamoDBAttribute
    private Float powerDemandCost;
    //Average daily consumption of entity in last 12 months
    @DynamoDBAttribute
    private Float averageDailyConsumption;

    //BillParameterInfo
    @DynamoDBAttribute
    private Float activePower;
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

    public Bill(BillDto billDto) {
        this.address = billDto.getAddress();
        this.fromDate = billDto.getFromDate();
        this.toDate = billDto.getToDate();
        this.totalPayable = billDto.getTotalPayable();
        this.activeEnergyCost = billDto.getActiveEnergyCost();
        this.producedCO2 = billDto.getProducedCO2();
        this.powerDemandCost = billDto.getPowerDemandCost();
        this.averageDailyConsumption = billDto.getAverageDailyConsumption();
       /* // this.activePower = billDto.getActivePower();
        this.aEFreeHours= billDto.getAEFreeHours().getParamId();
        this.aEOffHours = billDto.getAEOffHours().getParamId();
        this.aENormalHours = billDto.getAENormalHours().getParamId();
        this.aEPeakHours = billDto.getAEPeakHours().getParamId();
        this.rDPeakHours= billDto.getRDPeakHours().getParamId();
        this.rDContractedPower = billDto.getRDContractedPower().getParamId();
        this.rDReactivePower = billDto.getRDReactivePower().getParamId();*/
    }
}
