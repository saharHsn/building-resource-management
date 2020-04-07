package tech.builtrix.web.dtos.bill;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created By sahar at 12/2/19
 */

@JsonInclude
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillDto {
    private String buildingId;
    private String electricityCounterCode;
    private String companyTaxNumber;
    private String address;
    private Date fromDate;
    private Date toDate;
    private Integer year;
    private Integer fromMonth;
    private float totalPayable = 0;
    private float activeEnergyCost = 0;
    private float producedCO2 = 0;
    private float powerDemandCost = 0;
    private float averageDailyConsumption = 0;
    private float totalMonthlyConsumption = 0;
    private BillParameterDto aEFreeHours;
    private BillParameterDto rDFreeHours;
    private BillParameterDto rDOffHours;
    private BillParameterDto aEOffHours;
    private BillParameterDto aENormalHours;
    private BillParameterDto rDNormalHours;
    private BillParameterDto aEPeakHours;
    private BillParameterDto rDPeakHours;
    private BillParameterDto rDContractedPower;
    private BillParameterDto rDReactivePower;

    @Override
    public Object clone() throws CloneNotSupportedException {
        BillDto billDto = new BillDto();
        billDto.buildingId = this.buildingId;
        billDto.electricityCounterCode = this.electricityCounterCode;
        billDto.companyTaxNumber = this.getCompanyTaxNumber();
        billDto.address = this.address;
        billDto.fromDate = this.fromDate;
        billDto.toDate = this.toDate;
        billDto.year = this.year;
        billDto.address = this.address;
        billDto.fromMonth = this.fromMonth;
        billDto.totalPayable = this.totalPayable;
        billDto.activeEnergyCost = this.activeEnergyCost;
        billDto.producedCO2 = this.producedCO2;
        billDto.powerDemandCost = this.powerDemandCost;
        billDto.averageDailyConsumption = this.averageDailyConsumption;
        billDto.totalMonthlyConsumption = this.totalMonthlyConsumption;
        billDto.aEFreeHours = (BillParameterDto) this.aEFreeHours.clone();
        billDto.rDFreeHours = (BillParameterDto) this.rDFreeHours.clone();
        billDto.aEOffHours = (BillParameterDto) this.aEOffHours.clone();
        billDto.rDOffHours = (BillParameterDto) this.rDOffHours.clone();
        billDto.aENormalHours = (BillParameterDto) this.aENormalHours.clone();
        billDto.rDNormalHours = (BillParameterDto) this.rDNormalHours.clone();
        billDto.aEPeakHours = (BillParameterDto) this.aEPeakHours.clone();
        billDto.rDPeakHours = (BillParameterDto) this.rDPeakHours.clone();
        billDto.rDContractedPower = (BillParameterDto) this.rDContractedPower.clone();
        billDto.rDReactivePower = (BillParameterDto) this.rDReactivePower.clone();
        return billDto;
    }
}
