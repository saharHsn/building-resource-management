package tech.builtrix.dtos.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import tech.builtrix.dtos.bill.EnergyConsumptionIndex;

/**
 * Created By sahar at 12/11/19
 */
@JsonInclude
@Data
@AllArgsConstructor
public class EnergyConsumptionIndexDto {
    private EnergyConsumptionIndex consumptionArea;
    private EnergyConsumptionIndex consumptionCap;
    private EnergyConsumptionIndex cost;
    private EnergyConsumptionIndex energyEfficiencyLevel;
}
