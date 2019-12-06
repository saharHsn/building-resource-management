package tech.builtrix.controller.report;

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
public class ConsumptionDynamicDto {
    List<Float> data;
    TimePeriodType periodType;
    Long year;
    DatePartType datePartType;
    String color;
    String name;
}
