package tech.builtrix.service.bill;

/**
 * Created By sahar at 12/2/19
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.builtrix.base.GenericCrudServiceBase;
import tech.builtrix.dto.BillDto;
import tech.builtrix.dto.BillParameterDto;
import tech.builtrix.model.bill.Bill;
import tech.builtrix.model.bill.BillParameterInfo;
import tech.builtrix.repository.bill.BillRepository;

@Component
@Slf4j
public class BillService extends GenericCrudServiceBase<Bill, BillRepository> {
    private final BillParameterService billParameterService;

    @Autowired
    protected BillService(BillRepository repository, BillParameterService billParameterService) {
        super(repository);
        this.billParameterService = billParameterService;
    }

    public Bill save(BillDto billDto) {
        BillParameterDto aeFreeHoursDto = billDto.getAEFreeHours();
        BillParameterInfo aeFreeHours = this.billParameterService.save(aeFreeHoursDto);
        BillParameterDto aeNormalHoursDto = billDto.getAENormalHours();
        BillParameterInfo aeNormalHours = this.billParameterService.save(aeNormalHoursDto);
        BillParameterDto aeOffHoursDto = billDto.getAEOffHours();
        BillParameterInfo aeOffHours = this.billParameterService.save(aeOffHoursDto);
        BillParameterDto aePeakHoursDto = billDto.getAEPeakHours();
        BillParameterInfo aePeakHours = this.billParameterService.save(aePeakHoursDto);
        BillParameterDto rdContractedPowerDto = billDto.getRDContractedPower();
        BillParameterInfo rdContractedPower = this.billParameterService.save(rdContractedPowerDto);
        BillParameterDto rdPeakHoursDto = billDto.getRDPeakHours();
        BillParameterInfo rdPeakHours = this.billParameterService.save(rdPeakHoursDto);
        BillParameterDto rdReactivePowerDto = billDto.getRDReactivePower();
        BillParameterInfo rdReactivePower = this.billParameterService.save(rdReactivePowerDto);
        Bill bill = new Bill(billDto);
        bill.setAEPeakHours(aePeakHours.getId());
        bill.setAEOffHours(aeOffHours.getId());
        bill.setAEFreeHours(aeFreeHours.getId());
        bill.setAENormalHours(aeNormalHours.getId());
        bill.setRDContractedPower(rdContractedPower.getId());
        bill.setRDPeakHours(rdPeakHours.getId());
        bill.setRDReactivePower(rdReactivePower.getId());
        bill = this.repository.save(bill);
        return bill;
    }

    public void update(BillDto billDto) {
        save(billDto);
    }

}
