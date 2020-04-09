package tech.builtrix.services.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class BillParamInfo {
    String name;
    String fromToHour1;
    String fromToHour2;
    String cost1;
    String cost2;
    String currency;
    String percentage;
}
