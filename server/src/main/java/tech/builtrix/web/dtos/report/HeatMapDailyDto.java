package tech.builtrix.web.dtos.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@JsonInclude
@Data
@NoArgsConstructor
public class HeatMapDailyDto {
    List<DayOfWeek> yValues;
    List<Month> xValues;
    List<DailyMatrix> dataMatrix = new ArrayList<>();

}
