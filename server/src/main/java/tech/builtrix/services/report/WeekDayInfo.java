package tech.builtrix.services.report;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WeekDayInfo {
    private BillParamInfo SuperVazio;
    private BillParamInfo VazioNormal;
    private BillParamInfo Ponta;
    private BillParamInfo Cheia;
}
