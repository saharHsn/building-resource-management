package tech.builtrix.dtos.bill;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created By sahar at 12/9/19
 */

@JsonInclude
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyConsumptionIndex {
    Float baseline;
    Float thisMonth;
    Float propertiesTarget;
    Float nationalMedian;
}
