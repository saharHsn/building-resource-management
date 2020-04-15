package tech.builtrix.models.bill;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import lombok.Getter;
import lombok.Setter;
import tech.builtrix.base.EntityBase;
import tech.builtrix.models.EnumConverter;
import tech.builtrix.models.building.enums.ElectricityBillType;
import tech.builtrix.web.dtos.bill.BillDto;

import java.util.Date;

/**
 * Created By sahar at 11/30/19
 */

@Setter
@Getter
//@AllArgsConstructor
@DynamoDBTable(tableName = "Bill")
public class Bill extends EntityBase<Bill> {
    @DynamoDBAttribute
    private String buildingId;
    @DynamoDBAttribute
    private String address;
    //TODO check uniqueness of this code same building
    @DynamoDBAttribute
    private String companyTaxNumber;
    @DynamoDBAttribute
    private String electricityCounterCode;
    @DynamoDBTypeConverted(converter = EnumConverter.class)
    @DynamoDBAttribute(attributeName = "Type")
    private ElectricityBillType type;
    @DynamoDBAttribute
    // @DynamoDBIndexRangeKey
    private Date fromDate;
    @DynamoDBAttribute
    private Integer fromYear;
    @DynamoDBAttribute
    private Integer fromMonth;
    @DynamoDBAttribute
    private Date toDate;
    // totalPayable Amount;
    @DynamoDBAttribute
    private float totalPayable = 0f;
    @DynamoDBAttribute
    private float activeEnergyCost = 0f;
    @DynamoDBAttribute
    private float producedCO2 = 0f;
    // Redes
    @DynamoDBAttribute
    private float powerDemandCost = 0f;
    // Average daily consumption of entity in last 12 months
    @DynamoDBAttribute
    private float averageDailyConsumption = 0f;
    @DynamoDBAttribute
    private float totalMonthlyConsumption = 0f;
    // BillParameterInfo
    @DynamoDBAttribute
    private float activePower = 0f;
    // active power (Energia Ativa) AE --> Energia Ativa
    // Super Vasio
    @DynamoDBAttribute(attributeName = "aEOffHours")
    private String aEOffHours;
    // Vasio Normal
    @DynamoDBAttribute(attributeName = "aEFreeHours")
    private String aEFreeHours;
    @DynamoDBAttribute(attributeName = "rDFreeHours")
    private String rDFreeHours;
    @DynamoDBAttribute(attributeName = "rDOffHours")
    private String rDOffHours;

    @DynamoDBAttribute(attributeName = "rDNormalHours")
    private String rDNormalHours;

    // Cheia
    @DynamoDBAttribute(attributeName = "aENormalHours")
    private String aENormalHours;
    // Ponta
    @DynamoDBAttribute(attributeName = "aEPeakHours")
    private String aEPeakHours;

    // cost of network RD --> (Rede)
    // Cost of Power demand in peak hour : Potência Horas de Ponta
    @DynamoDBAttribute(attributeName = "rDPeakHours")
    private String rDPeakHours;
    // cost of contracted power: Potencia Contratada
    @DynamoDBAttribute(attributeName = "rDContractedPower")
    private String rDContractedPower;
    // ???? TODO ask Mr.Kamarlouei
    // cost of Energia Reativa: Reativa Fornecida no vazio (Vz)
    @DynamoDBAttribute(attributeName = "rDReactivePower")
    private String rDReactivePower;

    public Bill() {
    }

    public Bill(BillDto billDto) {
        this.buildingId = billDto.getBuildingId();
        this.electricityCounterCode = billDto.getElectricityCounterCode();
        this.companyTaxNumber = billDto.getCompanyTaxNumber();
        this.address = billDto.getAddress();
        this.fromDate = billDto.getFromDate();
        this.toDate = billDto.getToDate();
        this.fromYear = billDto.getYear();
        this.fromMonth = billDto.getFromMonth();
        this.totalPayable = billDto.getTotalPayable();
        this.activeEnergyCost = billDto.getActiveEnergyCost();
        this.producedCO2 = billDto.getProducedCO2();
        this.powerDemandCost = billDto.getPowerDemandCost();
        this.averageDailyConsumption = billDto.getAverageDailyConsumption();
        this.totalMonthlyConsumption = billDto.getTotalMonthlyConsumption();
    }
}
