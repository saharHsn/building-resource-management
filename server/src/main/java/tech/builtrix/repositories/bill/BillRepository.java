package tech.builtrix.repositories.bill;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import tech.builtrix.base.RepositoryBase;
import tech.builtrix.models.bill.Bill;

import java.util.List;

@EnableScan
public interface BillRepository extends RepositoryBase<Bill> {
    List<Bill> findByBuildingId(String buildingId);
}
