package tech.builtrix.models.historical;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedTimestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.builtrix.base.EntityBase;
import tech.builtrix.models.historical.enums.HourPeriod;
import tech.builtrix.models.historical.enums.HourPeriodConverter;
import tech.builtrix.web.dtos.historical.HistoricalEnergyConsumptionDto;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
//@AllArgsConstructor
@DynamoDBTable(tableName = "Historical_Energy_Consumption")
public class HistoricalConsumption extends EntityBase<HistoricalConsumption> implements Comparable {
    @DynamoDBAttribute
    private String buildingId;
    @DynamoDBAttribute(attributeName = "reportDate", mappedBy = "N")
    @DynamoDBTypeConvertedTimestamp
    private Date reportDate;
    @DynamoDBAttribute
    //24 h format 00 to 24
    private float hour;
    @DynamoDBAttribute
    private float consumption;
    @DynamoDBAttribute
    private float cost;
    @DynamoDBTypeConverted(converter = HourPeriodConverter.class)
    @DynamoDBAttribute(attributeName = "HourPeriod")
    private HourPeriod hourPeriod = HourPeriod.UNKNOWN;

    public HistoricalConsumption(String buildingId) {
        this.buildingId = buildingId;
    }

    public HistoricalConsumption(HistoricalEnergyConsumptionDto consumptionDto) {
        this.buildingId = consumptionDto.getBuildingId();
        this.reportDate = consumptionDto.getDate();
        this.hour = consumptionDto.getHour();
        this.consumption = consumptionDto.getConsumption();
        this.cost = consumptionDto.getCost();
        this.hourPeriod = consumptionDto.getHourPeriod();
    }

    @Override
    public String toString() {
        return "HistoricalConsumption{" +
                "buildingId='" + buildingId + '\'' +
                ", reportDate=" + reportDate +
                ", hour=" + hour +
                ", consumption=" + consumption +
                ", cost=" + cost +
                ", hourPeriod=" + hourPeriod +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        Date reportDate = ((HistoricalConsumption) o).getReportDate();
        if (this.reportDate == null || reportDate == null) {
            return 0;
        }
        return this.reportDate.compareTo(reportDate);
    }
}
