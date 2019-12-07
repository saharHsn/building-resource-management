package tech.builtrix.repositories.bill;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import tech.builtrix.base.RepositoryBase;
import tech.builtrix.models.bill.Bill;

@EnableScan
public interface BillRepository extends RepositoryBase<Bill> {
}
