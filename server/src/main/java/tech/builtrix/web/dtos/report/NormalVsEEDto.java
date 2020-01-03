package tech.builtrix.web.dtos.report;

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
public class NormalVsEEDto {
    List<String> xValues;
    List<Float> standardAValues;
    List<Float> standardBValues;
    List<Float> standardCValues;
    List<Float> standardDValues;
    List<Float> total;

}
