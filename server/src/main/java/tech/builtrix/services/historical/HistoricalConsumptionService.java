package tech.builtrix.services.historical;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.marshallers.DateToStringMarshaller;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.builtrix.base.GenericCrudServiceBase;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.models.historical.HistoricalConsumption;
import tech.builtrix.repositories.bill.HistoricalConsumptionRepository;
import tech.builtrix.utils.ReportUtil;
import tech.builtrix.web.dtos.historical.HistoricalEnergyConsumptionDto;
import tech.builtrix.web.dtos.report.HistoricalConsumptionDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    public List<HistoricalEnergyConsumptionDto> filterByDate(Date startDate, Date endDate) {
        AttributeValue start = DateToStringMarshaller.instance().marshall(startDate);
        AttributeValue end = DateToStringMarshaller.instance().marshall(endDate);
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("reportDate >= :start and reportDate < :end")
                .addExpressionAttributeValuesEntry(":start", start)
                .addExpressionAttributeValuesEntry(":end", end)
                .withConsistentRead(true);
        List<HistoricalConsumption> consumptions = mapper.scan(HistoricalConsumption.class, scanExpression);


       /* Map<String, String> attributeNames = new HashMap<>();
        attributeNames.put("#date", "date");

        Map<String, AttributeValue> attributeValues = new HashMap<>();
        attributeValues.put(":from", new AttributeValue().withN(startDate.toString()));
        attributeValues.put(":to", new AttributeValue().withN(endDate.toString()));

        DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression()
                .withFilterExpression("#date >= :from and #date <= :to")
                .withExpressionAttributeNames(attributeNames)
                .withExpressionAttributeValues(attributeValues);*/

        List<HistoricalEnergyConsumptionDto> historicalConsumptionDtos = new ArrayList<>();
        for (HistoricalConsumption historicalConsumption : consumptions) {
            historicalConsumptionDtos.add(new HistoricalEnergyConsumptionDto(historicalConsumption));
        }
        return historicalConsumptionDtos;
    }

    @Override
    public void delete(String id) throws NotFoundException {

    }

    public HistoricalConsumptionDto getHistoricalConsumption(Date from, Date to) {
        List<HistoricalEnergyConsumptionDto> historicalEnergyConsumptionDtos = filterByDate(from, to);
        return ReportUtil.getHistoricalConsumption(historicalEnergyConsumptionDtos);
    }

    public List<HistoricalConsumption> findAll() {
        Iterable<HistoricalConsumption> all = this.repository.findAll();
        return (List<HistoricalConsumption>) all;
    }

    public void update(HistoricalConsumption consumption) {
        this.repository.save(consumption);
    }
}
