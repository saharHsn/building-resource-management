package tech.builtrix.services.bill;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.builtrix.base.GenericCrudServiceBase;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.models.bill.BillParameterInfo;
import tech.builtrix.repositories.bill.BillParameterRepository;
import tech.builtrix.web.dtos.bill.BillParameterDto;

import java.util.Optional;

/**
 * Created By sahar at 12/4/19
 */
@Component
@Slf4j
public class BillParameterService extends GenericCrudServiceBase<BillParameterInfo, BillParameterRepository> {
    protected BillParameterService(BillParameterRepository repository) {
        super(repository);
    }

    public void delete(String id) throws NotFoundException {
        repository.delete(findById(id));
    }

    public BillParameterInfo findById(String id) throws NotFoundException {
        Optional<BillParameterInfo> opitonal = this.repository.findById(id);
        if (opitonal.isPresent()) {
            return opitonal.get();
        } else {
            throw new NotFoundException("bill param", "id", id);
        }
    }

	public BillParameterInfo save(BillParameterDto billParameterDto) {
		BillParameterInfo billParameterInfo = new BillParameterInfo(billParameterDto);
		billParameterInfo = this.repository.save(billParameterInfo);
		return billParameterInfo;
	}
}
