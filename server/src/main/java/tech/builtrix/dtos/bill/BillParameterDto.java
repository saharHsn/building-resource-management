package tech.builtrix.dtos.bill;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created By sahar at 12/2/19
 */

@JsonInclude
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillParameterDto {
    String paramId;
    private Date initialDate;
    private Date endDate;
    private Float cost;
    private Float consumption;
    private Float tariffPrice;
    private Float totalTariffCost;
}
