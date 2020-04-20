package tech.builtrix.services.historical;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.marshallers.DateToStringMarshaller;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.builtrix.base.GenericCrudServiceBase;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.models.historical.HistoricalConsumption;
import tech.builtrix.repositories.bill.HistoricalConsumptionRepository;
import tech.builtrix.services.report.DataType;
import tech.builtrix.utils.DateUtil;
import tech.builtrix.utils.ReportUtil;
import tech.builtrix.web.dtos.historical.HistoricalEnergyConsumptionDto;
import tech.builtrix.web.dtos.report.HistoricalConsumptionDto;

import java.util.*;

@Component
@Slf4j
public class HistoricalConsumptionService extends GenericCrudServiceBase<HistoricalConsumption, HistoricalConsumptionRepository> {
    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    static DynamoDBMapper mapper = new DynamoDBMapper(client);
    AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard().build();

    protected HistoricalConsumptionService(HistoricalConsumptionRepository repository) {
        super(repository);
    }

    public HistoricalEnergyConsumptionDto findById(String id) throws NotFoundException {
        Optional<HistoricalConsumption> consumption = this.repository.findById(id);
        if (consumption.isPresent()) {
            return new HistoricalEnergyConsumptionDto(consumption.get());
        } else {
            throw new NotFoundException("historicalConsumption", "id", id);
        }
    }

    public HistoricalConsumption save(HistoricalConsumption historicalConsumption) {
        // HistoricalConsumption historicalConsumption = new HistoricalConsumption(consumptionDto);
        historicalConsumption = this.repository.save(historicalConsumption);
        return historicalConsumption;
    }

    public List<HistoricalEnergyConsumptionDto> filterByDate(String buildingId, Date startDate, Date endDate) {
        AttributeValue start = DateToStringMarshaller.instance().marshall(startDate);
        AttributeValue end = DateToStringMarshaller.instance().marshall(endDate);

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":buildingId", new AttributeValue(buildingId));
        expressionAttributeValues.put(":start", start);
        expressionAttributeValues.put(":end", end);

        ScanRequest scanRequest = new ScanRequest().withTableName("Historical_Energy_Consumption")
                .withFilterExpression("buildingId = :buildingId and reportDate >= :start and reportDate < :end")
                .withExpressionAttributeValues(expressionAttributeValues);
        Map<String, AttributeValue> lastKey;
        List<Map<String, AttributeValue>> resultList = new ArrayList<>();
        int row = 0;
        long l = System.currentTimeMillis();
        do {
            ScanResult scanResult = amazonDynamoDB.scan(scanRequest);
            List<Map<String, AttributeValue>> results = scanResult.getItems();
            resultList.addAll(results);
            lastKey = scanResult.getLastEvaluatedKey();
            scanRequest.setExclusiveStartKey(lastKey);
        } while (lastKey != null);
        System.out.println("finished in : " + (System.currentTimeMillis() - l));

        List<HistoricalEnergyConsumptionDto> historicalConsumptionDtos = new ArrayList<>();
        for (Map<String, AttributeValue> stringAttributeValueMap : resultList) {
            historicalConsumptionDtos.add(new HistoricalEnergyConsumptionDto(stringAttributeValueMap, buildingId));
        }
        Collections.sort(historicalConsumptionDtos);
        return historicalConsumptionDtos;
    }

    @Override
    public void delete(String id) throws NotFoundException {

    }

    public HistoricalConsumptionDto getHistoricalConsumption(String buildingId, Integer year, Integer month, DataType dataType) {
        //make date from first day of month and another for last day of month
        String monthStr = month.toString().length() == 1 ? "0" + month : month.toString();
        String dateStr = year + "-" + monthStr + "-" + "01T" + "00:00:00";
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        //"2010-05-23T09:01:02"
        Date from = DateUtil.getDateFromPattern(dateStr, pattern);
        String dateStr1 = year + "-" + monthStr + "-" + DateUtil.getNumOfDaysOfMonth(year, month) + "T23:59:59";
        Date to = DateUtil.getDateFromPattern(dateStr1, pattern);
        List<HistoricalEnergyConsumptionDto> historicalEnergyConsumptionDtos = filterByDate(buildingId, from, to);
        return ReportUtil.getHistoricalConsumption(historicalEnergyConsumptionDtos, dataType);
    }

    public List<HistoricalConsumption> findAll() {
        Iterable<HistoricalConsumption> all = this.repository.findAll();
        return (List<HistoricalConsumption>) all;
    }

    public static void main(String[] args) {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Copenhagen");
        Integer year = 2020;
        Integer month = 3;
        String monthStr = month.toString().length() == 1 ? "0" + month : month.toString();
        String dateStr = year + "-" + monthStr + "-" + "01T" + "00:00:00";
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        //"2010-05-23T09:01:02"
        Date from = DateUtil.getDateFromPattern(dateStr, pattern);
        String dateStr1 = year + "-" + monthStr + "-" + DateUtil.getNumOfDaysOfMonth(year, month) + "T23:59:59";
        Date to = DateUtil.getDateFromPattern(dateStr1, pattern);
        AttributeValue start = DateToStringMarshaller.instance().marshall(from);
        AttributeValue end = DateToStringMarshaller.instance().marshall(to);
        System.out.println();
    }

    public void update(HistoricalConsumption consumption) {
        this.repository.save(consumption);
    }

}
