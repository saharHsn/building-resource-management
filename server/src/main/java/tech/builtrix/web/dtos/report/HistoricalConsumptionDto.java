package tech.builtrix.web.dtos.report;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude
@Data
@NoArgsConstructor
public class HistoricalConsumptionDto {
    List<String> xValues;
    List<Float> contractedPowerValues;
    List<Float> powerInPeakValues;
    List<Float> reactivePowerValues;
    List<Float> normalValues;
    List<Float> peakValues;
    List<Float> freeValues;
    List<Float> offValues;
}
