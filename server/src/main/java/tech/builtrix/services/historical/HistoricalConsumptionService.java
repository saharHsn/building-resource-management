package tech.builtrix.services.historical;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.builtrix.base.GenericCrudServiceBase;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.models.historical.HistoricalConsumption;
import tech.builtrix.repositories.bill.HistoricalConsumptionRepository;
import tech.builtrix.web.dtos.historical.HistoricalConsumptionDto;

import java.util.Optional;

@Component
@Slf4j
public class HistoricalConsumptionService extends GenericCrudServiceBase<HistoricalConsumption, HistoricalConsumptionRepository> {

    protected HistoricalConsumptionService(HistoricalConsumptionRepository repository) {
        super(repository);
    }

    public HistoricalConsumptionDto findById(String id) throws NotFoundException {
        Optional<HistoricalConsumption> consumption = this.repository.findById(id);
        if (consumption.isPresent()) {
            return new HistoricalConsumptionDto(consumption.get());
        } else {
            throw new NotFoundException("historicalConsumption", "id", id);
        }
    }

    public HistoricalConsumption save(HistoricalConsumptionDto consumptionDto) {
        HistoricalConsumption historicalConsumption = new HistoricalConsumption(consumptionDto);
        historicalConsumption = this.repository.save(historicalConsumption);
        return historicalConsumption;
    }


    @Override
    public void delete(String id) throws NotFoundException {

    }
}
