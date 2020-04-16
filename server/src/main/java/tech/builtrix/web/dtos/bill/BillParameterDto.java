package tech.builtrix.web.dtos.bill;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.builtrix.models.bill.BillParameterInfo;
import tech.builtrix.models.bill.enums.ParameterType;

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
    private float cost = 0f;
    private float consumption = 0f;
    private float tariffPrice = 0f;
    private float totalTariffCost = 0f;
    private ParameterType paramType;

    public BillParameterDto(BillParameterInfo billParameterInfo) {
        this.initialDate = billParameterInfo.getInitialDate();
        this.consumption = billParameterInfo.getConsumption();
        this.cost = billParameterInfo.getCost();
        this.endDate = billParameterInfo.getEndDate();
        this.paramId = billParameterInfo.getId();
        this.tariffPrice = billParameterInfo.getTariffPrice();
        this.totalTariffCost = billParameterInfo.getTotalTariffCost();
        this.paramType = billParameterInfo.getParameterType();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        BillParameterDto parameterDto = new BillParameterDto();
        parameterDto.consumption = this.consumption;
        parameterDto.cost = this.cost;
        parameterDto.initialDate = this.initialDate;
        parameterDto.endDate = this.endDate;
        parameterDto.tariffPrice = this.tariffPrice;
        parameterDto.totalTariffCost = this.totalTariffCost;
        parameterDto.paramType = this.paramType;
        return parameterDto;
    }
}
