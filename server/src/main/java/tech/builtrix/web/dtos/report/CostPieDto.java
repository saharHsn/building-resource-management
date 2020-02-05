package tech.builtrix.web.dtos.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created By sahar at 12/6/19
 */

@JsonInclude
@Data
@NoArgsConstructor
public class CostPieDto {
	Float contractedPower;
	Float offHours;
	Float freeHours;
	Float peakHours;
	Float normalHours;
	Float powerInPeakHours;
	Float reactivePower;
}
