package tech.builtrix.web.dtos.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude
@Data
@NoArgsConstructor
public class CurrentMonthSummaryDto {
    private Float consumption;
    private Float cost;
    private Float environmental;
}
