package tech.builtrix.models.historical;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import lombok.Getter;
import lombok.Setter;
import tech.builtrix.base.EntityBase;
import tech.builtrix.web.dtos.historical.HistoricalConsumptionDto;

import java.util.Date;

@Setter
@Getter
//@AllArgsConstructor
@DynamoDBTable(tableName = "Historical_Energy_Consumption")
public class HistoricalConsumption extends EntityBase<HistoricalConsumption> {
    @DynamoDBAttribute
    private String buildingId;
    @DynamoDBAttribute
    private Date date;
    @DynamoDBAttribute
    //24 h format 00 to 24
    private float hour;
    @DynamoDBAttribute
    private float consumption;
    @DynamoDBTypeConverted(converter = HourPeriodConverter.class)
    @DynamoDBAttribute(attributeName = "HourPeriod")
    private HourPeriod hourPeriod = HourPeriod.UNKNOWN;

    public HistoricalConsumption(String buildingId) {
        this.buildingId = buildingId;
    }

    public HistoricalConsumption(HistoricalConsumptionDto consumptionDto) {
        this.buildingId = consumptionDto.getBuildingId();
        this.date = consumptionDto.getDate();
        this.hour = consumptionDto.getHour();
        this.consumption = consumptionDto.getConsumption();
        this.hourPeriod = consumptionDto.getHourPeriod();
    }
}
