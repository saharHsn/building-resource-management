package tech.builtrix.repositories.bill;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import tech.builtrix.base.RepositoryBase;
import tech.builtrix.models.historical.HistoricalConsumption;

@EnableScan
public interface HistoricalConsumptionRepository extends RepositoryBase<HistoricalConsumption> {
}
