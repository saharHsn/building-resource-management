package tech.builtrix.web.dtos.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonInclude
@Data
@NoArgsConstructor
public class HeatMapHourlyDto {
    List<Integer> xValues;
    List<Integer> yValues;
    List<int[]> dataMatrix;
}
