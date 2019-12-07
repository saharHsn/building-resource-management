package tech.builtrix.controllers.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created By sahar at 12/6/19
 */

@JsonInclude
@Data
@NoArgsConstructor
public class PredictedWeatherVsRealDto {
    List<String> xValues;
    List<Float> baseLineValues;
    List<Float> consumptionValues;
}
