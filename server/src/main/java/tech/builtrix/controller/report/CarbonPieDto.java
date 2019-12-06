package tech.builtrix.controller.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created By sahar at 12/6/19
 */

@JsonInclude
@Data
@NoArgsConstructor
public class CarbonPieDto {
    Float co2Free;
    Float co2Peak;
    Float co2Normal;
    Float co2Off;

}
