package tech.builtrix.controller.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created By sahar at 12/4/19
 */

@JsonInclude
@Data
@NoArgsConstructor
public class PredictionDto {
    List<String> xValues;
    List<Float> costYValues;
    List<Float> savingYValues;
    String buildingId;
}