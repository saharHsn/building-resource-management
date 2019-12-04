package tech.builtrix.dto;

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
    private String address;
    private Date fromDate;
    private Date toDate;
    private Float totalPayable;
    private Float activeEnergyCost;
    private Float producedCO2;
    private Float powerDemandCost;
    private Float averageDailyConsumption;
    private BillParameterDto aEOffHours;
    private BillParameterDto aEFreeHours;
    private BillParameterDto aENormalHours;
    private BillParameterDto aEPeakHours;
    private BillParameterDto rDPeakHours;
    private BillParameterDto rDContractedPower;
    private BillParameterDto rDReactivePower;
}
