package tech.builtrix.repositories.bill;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import tech.builtrix.base.RepositoryBase;
import tech.builtrix.models.bill.BillParameterInfo;

@EnableScan
public interface BillParameterRepository extends RepositoryBase<BillParameterInfo> {
}
