package tech.builtrix.dtos.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created By sahar at 12/6/19
 */

@JsonInclude
@Data
@NoArgsConstructor
public class CarbonSPLineDto {
    //'Jan-2018',
    //                'Feb-2018',
    //                'Mar-2018',
    //                'Apr-2018',
    //                'May-2018',
    //                'Jun-2018',
    //                'Jul-2018',
    //                'Aug-2018',
    //                'Sep-2018',
    //                'Oct-2018',
    //                'Nov-2018',
    //                'Dec-2018'
    List<String> xValues;
    List<Float> totalValues;
    List<Float> baseLineValues;

}
