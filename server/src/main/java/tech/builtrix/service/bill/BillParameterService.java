package tech.builtrix.service.bill;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.builtrix.base.GenericCrudServiceBase;
import tech.builtrix.dto.BillParameterDto;
import tech.builtrix.model.bill.BillParameterInfo;
import tech.builtrix.repository.bill.BillParameterRepository;

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
