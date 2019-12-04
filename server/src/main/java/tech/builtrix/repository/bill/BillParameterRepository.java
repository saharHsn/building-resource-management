package tech.builtrix.repository.bill;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import tech.builtrix.base.RepositoryBase;
import tech.builtrix.model.bill.BillParameterInfo;

@EnableScan
public interface BillParameterRepository extends RepositoryBase<BillParameterInfo> {
}
