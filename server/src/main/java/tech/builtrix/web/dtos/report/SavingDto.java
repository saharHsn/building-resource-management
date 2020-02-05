package tech.builtrix.web.dtos.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created By sahar at 12/5/19
 */
@JsonInclude
@Data
@NoArgsConstructor
public class SavingDto {
	private Float consumption;
	private Float cost;
	private Float environmental;
}