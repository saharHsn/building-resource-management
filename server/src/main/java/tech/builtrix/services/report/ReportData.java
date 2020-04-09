package tech.builtrix.services.report;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ReportData {
    List<Object> sVList;
    List<Object> vNList;
    List<Object> pList;
    List<Object> cList;
    List<Object> totList;
}
