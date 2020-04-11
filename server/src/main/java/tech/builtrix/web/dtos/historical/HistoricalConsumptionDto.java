package tech.builtrix.web.dtos.historical;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.builtrix.models.historical.HistoricalConsumption;
import tech.builtrix.models.historical.HourPeriod;

import java.util.Date;

/**
 * Created By sahar at 12/2/19
 */

@JsonInclude
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalConsumptionDto {
    private String buildingId;
    private Date date;
    //24 h format 00 to 24
    private float hour;
    private float consumption;
    private HourPeriod hourPeriod = HourPeriod.UNKNOWN;

    public HistoricalConsumptionDto(HistoricalConsumption historicalConsumption) {
        this.buildingId = historicalConsumption.getBuildingId();
        this.date = historicalConsumption.getDate();
        this.hour = historicalConsumption.getHour();
        this.consumption = historicalConsumption.getConsumption();
        this.hourPeriod = historicalConsumption.getHourPeriod();
    }
}
