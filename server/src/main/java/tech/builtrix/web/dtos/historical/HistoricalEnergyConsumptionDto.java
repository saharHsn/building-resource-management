package tech.builtrix.web.dtos.historical;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.builtrix.models.historical.HistoricalConsumption;
import tech.builtrix.models.historical.enums.HourPeriod;
import tech.builtrix.utils.DateUtil;

import java.util.Date;
import java.util.Map;

/**
 * Created By sahar at 12/2/19
 */

@JsonInclude
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalEnergyConsumptionDto implements Comparable {
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

    public HistoricalEnergyConsumptionDto(Map<String, AttributeValue> historyMap, String buildingId) {
        this.cost = Float.parseFloat(historyMap.get("cost").getN());
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        //2020-03-27T21:30:00.000Z
        Date reportDate = DateUtil.getDateFromPattern(historyMap.get("reportDate").getS(), pattern);
        this.date = reportDate;
        this.hour = Float.parseFloat(historyMap.get("hour").getN());
        this.hourPeriod = HourPeriod.valueOf(historyMap.get("HourPeriod").getS());
        this.consumption = Float.parseFloat(historyMap.get("consumption").getN());
        this.buildingId = buildingId;

    }

    @Override
    public int compareTo(Object o) {
        Date reportDate = ((HistoricalEnergyConsumptionDto) o).getDate();
        if (this.date == null || reportDate == null) {
            return 0;
        }
        return this.date.compareTo(reportDate);
    }
}
