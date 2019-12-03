package tech.builtrix.util;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Created By sahar at 12/3/19
 */
@Setter
@Getter
public class MyTable {
    Map<String, List<String>> column_value;
    List<String> rowHeaders;
}
