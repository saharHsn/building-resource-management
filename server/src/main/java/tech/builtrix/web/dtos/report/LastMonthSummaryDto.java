package tech.builtrix.web.dtos.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude
@Data
@NoArgsConstructor
public class LastMonthSummaryDto {
    private Float consumption;
    private Float cost;
    private Float environmental;
    private String lastMonth;
}
