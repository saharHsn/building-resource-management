package tech.builtrix.services.historical;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.datamodeling.marshallers.DateToStringMarshaller;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.builtrix.base.GenericCrudServiceBase;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.models.historical.HistoricalConsumption;
import tech.builtrix.repositories.bill.HistoricalConsumptionRepository;
import tech.builtrix.services.report.DataType;
import tech.builtrix.utils.ReportUtil;
import tech.builtrix.web.dtos.historical.HistoricalEnergyConsumptionDto;
import tech.builtrix.web.dtos.report.HistoricalConsumptionDto;

import java.util.*;

@Component
@Slf4j
public class HistoricalConsumptionService extends GenericCrudServiceBase<HistoricalConsumption, HistoricalConsumptionRepository> {
    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    static DynamoDBMapper mapper = new DynamoDBMapper(client);

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
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("buildingId = :buildingId and reportDate >= :start and reportDate < :end")
                .addExpressionAttributeValuesEntry(":buildingId", new AttributeValue(buildingId))
                .addExpressionAttributeValuesEntry(":start", start)
                .addExpressionAttributeValuesEntry(":end", end)
                .withConsistentRead(true);
        PaginatedScanList<HistoricalConsumption> scanResult = mapper.scan(HistoricalConsumption.class, scanExpression);
        scanResult.loadAllResults();
        List<HistoricalConsumption> historicalConsumptions = new ArrayList<>(scanResult.size());
        // scanResult.sort(HistoricalConsumption::compareTo);
        historicalConsumptions.addAll(scanResult);
        Collections.sort(historicalConsumptions);
        List<HistoricalEnergyConsumptionDto> historicalConsumptionDtos = new ArrayList<>();
        for (HistoricalConsumption historicalConsumption : historicalConsumptions) {
            historicalConsumptionDtos.add(new HistoricalEnergyConsumptionDto(historicalConsumption));
        }
        return historicalConsumptionDtos;
    }

    @Override
    public void delete(String id) throws NotFoundException {

    }

    public HistoricalConsumptionDto getHistoricalConsumption(String buildingId, Date from, Date to, DataType dataType) {
        List<HistoricalEnergyConsumptionDto> historicalEnergyConsumptionDtos = filterByDate(buildingId, from, to);
        return ReportUtil.getHistoricalConsumption(historicalEnergyConsumptionDtos, dataType);
    }

    public List<HistoricalConsumption> findAll() {
        Iterable<HistoricalConsumption> all = this.repository.findAll();
        return (List<HistoricalConsumption>) all;
    }

    public void update(HistoricalConsumption consumption) {
        this.repository.save(consumption);
    }

}
