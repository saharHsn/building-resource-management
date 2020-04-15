package tech.builtrix.web.dtos.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.builtrix.web.dtos.report.enums.DatePartType;
import tech.builtrix.web.dtos.report.enums.TimePeriodType;

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
