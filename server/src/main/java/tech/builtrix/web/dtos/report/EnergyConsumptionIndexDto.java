package tech.builtrix.web.dtos.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import tech.builtrix.web.dtos.bill.ReportIndex;

/**
 * Created By sahar at 12/11/19
 */
@JsonInclude
@Data
@AllArgsConstructor
public class EnergyConsumptionIndexDto {
    private ReportIndex consumptionArea;
    private ReportIndex consumptionCap;
    private ReportIndex cost;
    private ReportIndex co2;
    private ReportIndex energyEfficiencyLevel;
}
