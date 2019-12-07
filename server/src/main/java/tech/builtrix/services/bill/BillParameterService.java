package tech.builtrix.services.bill;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.builtrix.base.GenericCrudServiceBase;
import tech.builtrix.dtos.bill.BillParameterDto;
import tech.builtrix.models.bill.BillParameterInfo;
import tech.builtrix.repositories.bill.BillParameterRepository;

/**
 * Created By sahar at 12/4/19
 */
@Component
@Slf4j
public class BillParameterService extends GenericCrudServiceBase<BillParameterInfo, BillParameterRepository> {
    protected BillParameterService(BillParameterRepository repository) {
        super(repository);
    }

    public BillParameterInfo save(BillParameterDto billParameterDto) {
        BillParameterInfo billParameterInfo = new BillParameterInfo(billParameterDto);
        billParameterInfo = this.repository.save(billParameterInfo);
        return billParameterInfo;
    }
}
