package tech.builtrix.service.bill;

/**
 * Created By sahar at 12/2/19
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.builtrix.base.GenericCrudServiceBase;
import tech.builtrix.model.bill.Bill;
import tech.builtrix.repository.bill.BillRepository;

@Component
@Slf4j
public class BillService extends GenericCrudServiceBase<Bill, BillRepository> {
    protected BillService(BillRepository repository) {
        super(repository);
    }
}
