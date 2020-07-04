package tech.builtrix.web.dtos.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.Month;

@AllArgsConstructor
@JsonInclude
@Data
@NoArgsConstructor
public class DailyMatrix {
    DayOfWeek dayOfWeek;
    Month month;
    float consumption;
}
