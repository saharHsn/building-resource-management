package tech.builtrix.web.dtos.historical;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.builtrix.models.historical.HistoricalConsumption;
import tech.builtrix.models.historical.enums.HourPeriod;

import java.util.Date;

/**
 * Created By sahar at 12/2/19
 */

@JsonInclude
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalEnergyConsumptionDto {
    private String buildingId;
    private Date date;
    //24 h format 00 to 24
    private float hour;
    private float consumption;
    private float cost;
    private HourPeriod hourPeriod = HourPeriod.UNKNOWN;

    public HistoricalEnergyConsumptionDto(HistoricalConsumption historicalConsumption) {
        this.buildingId = historicalConsumption.getBuildingId();
        this.date = historicalConsumption.getReportDate();
        this.hour = historicalConsumption.getHour();
        this.consumption = historicalConsumption.getConsumption();
        this.cost = historicalConsumption.getCost();
        this.hourPeriod = historicalConsumption.getHourPeriod();
    }
}
