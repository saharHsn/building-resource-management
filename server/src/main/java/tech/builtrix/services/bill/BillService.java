package tech.builtrix.services.bill;

/**
 * Created By sahar at 12/2/19
 */

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tech.builtrix.base.GenericCrudServiceBase;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.models.bill.Bill;
import tech.builtrix.models.bill.BillParameterInfo;
import tech.builtrix.repositories.bill.BillRepository;
import tech.builtrix.utils.DateUtil;
import tech.builtrix.web.dtos.bill.BillDto;
import tech.builtrix.web.dtos.bill.BillParameterDto;
import tech.builtrix.web.dtos.bill.BuildingDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Slf4j
public class BillService extends GenericCrudServiceBase<Bill, BillRepository> {
    private static String billTableName = Bill.class.getAnnotation(DynamoDBTable.class).tableName();
    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    static DynamoDBMapper mapper = new DynamoDBMapper(client);
    private static String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private final BillParameterService billParameterService;
    // private final BuildingService buildingService;

    @Autowired
    protected BillService(BillRepository repository, BillParameterService billParameterService) {
        super(repository);
        this.billParameterService = billParameterService;
    }


    public static void main(String[] args) throws ParseException {
        BillService billService = new BillService(null, null);
        // billService.filterByFromDateAndMonthAndBuilding(new Date(), 1, 2, 4,
        // "999999");
        billService.filterByFromDateAndMonthAndBuilding(2019, 1, 2, 4, "999999");
    }

    public Bill findById(String id) throws NotFoundException {
        Optional<Bill> optional = this.repository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new NotFoundException("bill", "id", id);
        }
    }

    public Bill save(BillDto billDto) {
        BillParameterDto aeFreeHoursDto = billDto.getAEFreeHours();
        BillParameterInfo aeFreeHours = null;
        if (aeFreeHoursDto != null) {
            aeFreeHours = this.billParameterService.save(aeFreeHoursDto);
        }
        BillParameterDto aeNormalHoursDto = billDto.getAENormalHours();
        BillParameterInfo aeNormalHours = null;
        if (aeNormalHoursDto != null) {
            aeNormalHours = this.billParameterService.save(aeNormalHoursDto);
        }
        BillParameterDto aeOffHoursDto = billDto.getAEOffHours();
        BillParameterInfo aeOffHours = null;
        if (aeOffHoursDto != null) {
            aeOffHours = this.billParameterService.save(aeOffHoursDto);
        }
        BillParameterDto aePeakHoursDto = billDto.getAEPeakHours();
        BillParameterInfo aePeakHours = null;
        if (aePeakHoursDto != null) {
            aePeakHours = this.billParameterService.save(aePeakHoursDto);
        }
        BillParameterDto rdContractedPowerDto = billDto.getRDContractedPower();
        BillParameterInfo rdContractedPower = null;
        if (rdContractedPowerDto != null) {
            rdContractedPower = this.billParameterService.save(rdContractedPowerDto);
        }
        BillParameterDto rdPeakHoursDto = billDto.getRDPeakHours();
        BillParameterInfo rdPeakHours = null;
        if (rdPeakHoursDto != null) {
            rdPeakHours = this.billParameterService.save(rdPeakHoursDto);
        }
        BillParameterDto rdPowerPeakHoursDto = billDto.getRDPowerPeakHours();
        BillParameterInfo rdPowerPeakHours = null;
        if (rdPowerPeakHoursDto != null) {
            rdPowerPeakHours = this.billParameterService.save(rdPowerPeakHoursDto);
        }
        BillParameterDto rdReactivePowerDto = billDto.getRDReactivePower();
        BillParameterInfo rdReactivePower = null;
        if (rdReactivePowerDto != null) {
            rdReactivePower = this.billParameterService.save(rdReactivePowerDto);
        }
        BillParameterDto rdOffHoursDto = billDto.getRDOffHours();
        BillParameterInfo rdOffHours = null;
        if (rdOffHoursDto != null) {
            rdOffHours = this.billParameterService.save(rdOffHoursDto);
        }
        BillParameterDto rdFreeHoursDto = billDto.getRDFreeHours();
        BillParameterInfo rdFreeHours = null;
        if (rdFreeHoursDto != null) {
            rdFreeHours = this.billParameterService.save(rdFreeHoursDto);
        }
        BillParameterDto rdNormalHoursDto = billDto.getRDNormalHours();
        BillParameterInfo rdNormalHours = null;
        if (rdNormalHoursDto != null) {
            rdNormalHours = this.billParameterService.save(rdNormalHoursDto);
        }
        Bill bill = new Bill(billDto);
        bill.setAEPeakHours(aePeakHours != null ? aePeakHours.getId() : null);
        bill.setAEOffHours(aeOffHours != null ? aeOffHours.getId() : null);
        bill.setRDOffHours(rdOffHours != null ? rdOffHours.getId() : null);
        bill.setRDFreeHours(rdFreeHours != null ? rdFreeHours.getId() : null);
        bill.setRDFreeHours(rdNormalHours != null ? rdNormalHours.getId() : null);
        bill.setAEFreeHours(aeFreeHours != null ? aeFreeHours.getId() : null);
        bill.setAENormalHours(aeNormalHours != null ? aeNormalHours.getId() : null);
        bill.setRDContractedPower(rdContractedPower != null ? rdContractedPower.getId() : null);
        bill.setRDPeakHours(rdPeakHours != null ? rdPeakHours.getId() : null);
        bill.setRDReactivePower(rdReactivePower != null ? rdReactivePower.getId() : null);
        bill.setRDPowerPeakHours(rdPowerPeakHours != null ? rdPowerPeakHours.getId() : null);
        bill.setRDNormalHours(rdNormalHours != null ? rdNormalHours.getId() : null);
        bill = this.repository.save(bill);
        return bill;
    }

    public void update(BillDto billDto) {
        save(billDto);
    }

    public List<Bill> findByBuilding(String buildingId) {
        List<Bill> bills;
        bills = this.repository.findByBuildingId(buildingId);
        return bills;
    }

    public List<Bill> filterByFromDateAndMonthAndBuilding(Integer year, Integer month1, Integer month2, Integer month3,
                                                          String buildingId) {
        // String fromDateStr = getDateStr(fromDate);
        Map<String, AttributeValue> exprAtrVals = new HashMap<>();
        exprAtrVals.put(":year", new AttributeValue().withN(year.toString()));
        exprAtrVals.put(":buildingId", new AttributeValue().withS(buildingId));
        exprAtrVals.put(":month1", new AttributeValue().withN(month1.toString()));
        exprAtrVals.put(":month2", new AttributeValue().withN(month2.toString()));
        exprAtrVals.put(":month3", new AttributeValue().withN(month3.toString()));
        String filterExpression = "buildingId = :buildingId and fromYear = :year "
                + "and fromMonth in (:month1, :month2, :month3)";
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression(filterExpression)
                .withExpressionAttributeValues(exprAtrVals);

        return mapper.scan(Bill.class, scanExpression);
    }

    public List<Bill> filterByFromDateAndMonthAndBuilding(Date fromDate, int month, String buildingId) {
        String fromDateStr = getDateStr(fromDate);
        Map<String, AttributeValue> exprAtrVals = new HashMap<>();
        exprAtrVals.put(":from_date", new AttributeValue().withS(fromDateStr));
        exprAtrVals.put(":buildingId", new AttributeValue().withS(buildingId));
        exprAtrVals.put(":month", new AttributeValue().withN(String.valueOf(month)));
        String filterExpression = "buildingId = :buildingId and fromDate <= :from_date " + "and fromMonth = :month";
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression(filterExpression)
                .withExpressionAttributeValues(exprAtrVals);
        return mapper.scan(Bill.class, scanExpression);
    }

    public Bill getLastBill(String buildingId) throws NotFoundException {
        // TODO find a way for fetching data order by fromDate
        Map<String, AttributeValue> exprAtrVals = new HashMap<>();
        exprAtrVals.put(":buildingId", new AttributeValue().withS(buildingId));
        String filterExpression = "buildingId = :buildingId ";
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression(filterExpression)
                .withExpressionAttributeValues(exprAtrVals);
        PaginatedScanList<Bill> bills = mapper.scan(Bill.class, scanExpression);
        Date maxDate = DateUtil.increaseDate(new Date(), -10, DateUtil.DateType.YEAR);
        Bill lastBill = null;
        for (Bill bill : bills) {
            if (bill.getToDate().after(maxDate)) {
                maxDate = bill.getToDate();
                lastBill = bill;
            }
        }
        return lastBill;
    }

    public BillDto getLastBillDto(String buildingId) throws NotFoundException {
        return convertBillToDto(getLastBill(buildingId));
    }

    public BillDto filterByMonthAndYear(String buildingId, int month, int year) throws NotFoundException {
        BillDto allBillInfo = null;
        Map<String, AttributeValue> exprAtrVals = new HashMap<>();
        exprAtrVals.put(":buildingId", new AttributeValue().withS(buildingId));
        exprAtrVals.put(":fromMonth", new AttributeValue().withN(String.valueOf(month)));
        exprAtrVals.put(":fromYear", new AttributeValue().withN(String.valueOf(year)));
        String filterExpression = "buildingId = :buildingId " + "and fromMonth = :fromMonth and fromYear = :fromYear";
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression(filterExpression)
                .withExpressionAttributeValues(exprAtrVals);
        PaginatedScanList<Bill> scan = mapper.scan(Bill.class, scanExpression);
        if (!CollectionUtils.isEmpty(scan)) {
            Bill bill = scan.get(0);
            allBillInfo = convertBillToDto(bill);
        }
        return allBillInfo;
    }

    public List<BillDto> getBillsOfLast12Months(String buildingId) throws NotFoundException {
        //Get last 12 months bills
        int month;
        int currentYear;
        List<BillDto> billDtos = new ArrayList<>();
        month = DateUtil.getCurrentMonth() - 1;
        currentYear = DateUtil.getCurrentYear();
        if (month == 12) {
            billDtos = getBillsOfYear(buildingId, currentYear, false);
        } else {
            List<BillDto> currentYearBills = getBillsOfYear(buildingId, currentYear, false);
            List<BillDto> lastYearBills = getBillsOfYear(buildingId, currentYear - 1, true);
            do {
                billDtos.add(currentYearBills.get(month - 1));
                month -= 1;
            } while (month >= 1);
            if (billDtos.size() < 12) {
                month = 12;
                do {
                    billDtos.add(lastYearBills.get(month - 1));
                    month -= 1;
                } while (billDtos.size() < 12);
            }
        }
        return billDtos;
    }


    // ----------------------------------------- Private Methods
    // ---------------------------------------------

    public List<BillDto> getBillsOfYear(String buildingId, Integer year, boolean fillMissedBills) throws NotFoundException {
        List<Bill> bills;
        List<BillDto> billDots = new ArrayList<>(Collections.nCopies(12, new BillDto()));
        bills = filterByYear(buildingId, year);
        for (Bill bill : bills) {
            billDots.set(bill.getFromMonth() - 1, convertBillToDto(bill));
        }
        //Try to fill bills that are null from average of not null bills
        if (fillMissedBills) {
            fillMissingBills(buildingId, billDots, bills);
        }
        return billDots;
    }

    private void fillMissingBills(String buildingId, List<BillDto> billDtos, List<Bill> bills) {
        int billsSize = bills.size();
        if (billsSize >= 1 && billsSize < 12) {
            List<BillDto> notNullBills = new ArrayList<>();
            List<Integer> notNullMonths = new ArrayList<>();
            for (Bill bill : bills) {
                notNullMonths.add(bill.getFromMonth());
                notNullBills.add(billDtos.get(bill.getFromMonth() - 1));
            }
            BillDto averageBill = makeAverageBill(buildingId, billsSize, notNullBills);
            addAverageBill(billDtos, notNullMonths, averageBill);
        }
    }

    private void addAverageBill(List<BillDto> billDots, List<Integer> notNullMonths, BillDto averageBill) {
        for (int i = 1; i <= 12; i++) {
            if (!notNullMonths.contains(i)) {
                BillDto dto;
                try {
                    dto = (BillDto) averageBill.clone();
                } catch (CloneNotSupportedException e) {
                    billDots.add(averageBill.setFromMonth(i));
                    continue;
                }
                dto.setFromMonth(i);
                billDots.set(i - 1, dto);
            }
        }
    }

    private BillDto makeAverageBill(String buildingId, int billSize, List<BillDto> notNullBills) {
        BillDto averageBill = new BillDto();
        BillParameterDto rdNormalHours = new BillParameterDto();
        BillParameterDto rdPeakHours = new BillParameterDto();
        BillParameterDto rdOffHours = new BillParameterDto();
        BillParameterDto rdFreeHours = new BillParameterDto();
        BillParameterDto rdContractedPower = new BillParameterDto();
        BillParameterDto rdReactivePower = new BillParameterDto();
        BillParameterDto aeNormalHours = new BillParameterDto();
        BillParameterDto aePeakHours = new BillParameterDto();
        BillParameterDto aeOffHours = new BillParameterDto();
        BillParameterDto aeFreeHours = new BillParameterDto();

        float totalMonthlyConsumption = 0f;
        float totalPayable = 0f;
        float activeEnergyCost = 0f;
        float averageDailyConsumption = 0f;
        float producedCO2 = 0f;
        float powerDemandCost = 0f;
        for (BillDto billMonth : notNullBills) {
            if (averageBill.getToDate() == null) {
                averageBill.setToDate(billMonth.getToDate());
            }
            if (averageBill.getFromDate() == null) {
                averageBill.setFromDate(billMonth.getFromDate());
            }
            if (averageBill.getAddress() == null) {
                averageBill.setAddress(billMonth.getAddress());
            }
            if (averageBill.getElectricityCounterCode() == null) {
                averageBill.setElectricityCounterCode(billMonth.getElectricityCounterCode());
            }
            if (averageBill.getBuildingId() == null) {
                averageBill.setBuildingId(buildingId);
            }
            if (averageBill.getCompanyTaxNumber() == null) {
                averageBill.setCompanyTaxNumber(billMonth.getCompanyTaxNumber());
            }
            if (averageBill.getYear() == null) {
                averageBill.setYear(billMonth.getYear());
            }
            totalMonthlyConsumption += billMonth.getTotalMonthlyConsumption();
            totalPayable += billMonth.getTotalPayable();
            activeEnergyCost += billMonth.getActiveEnergyCost();
            averageDailyConsumption += billMonth.getAverageDailyConsumption();
            producedCO2 += billMonth.getProducedCO2();
            powerDemandCost += billMonth.getPowerDemandCost();

            rdNormalHours = mergeBillParam(rdNormalHours, billMonth.getRDNormalHours());
            rdPeakHours = mergeBillParam(rdPeakHours, billMonth.getRDPeakHours());
            rdOffHours = mergeBillParam(rdOffHours, billMonth.getRDOffHours());
            rdFreeHours = mergeBillParam(rdFreeHours, billMonth.getRDFreeHours());
            rdContractedPower = mergeBillParam(rdContractedPower, billMonth.getRDContractedPower());
            rdReactivePower = mergeBillParam(rdReactivePower, billMonth.getRDReactivePower());
            aeNormalHours = mergeBillParam(aeNormalHours, billMonth.getAENormalHours());
            aePeakHours = mergeBillParam(aePeakHours, billMonth.getAEPeakHours());
            aeOffHours = mergeBillParam(aeOffHours, billMonth.getAEOffHours());
            aeFreeHours = mergeBillParam(aeFreeHours, billMonth.getAEFreeHours());

        }

        averageBill.setTotalMonthlyConsumption(totalMonthlyConsumption / billSize);
        averageBill.setTotalPayable(totalPayable / billSize);
        averageBill.setActiveEnergyCost(activeEnergyCost / billSize);
        averageBill.setAverageDailyConsumption(averageDailyConsumption / billSize);
        averageBill.setProducedCO2(producedCO2 / billSize);
        averageBill.setPowerDemandCost(powerDemandCost / billSize);

        averageBill.setRDNormalHours(averageBillParameter(rdNormalHours, billSize));
        averageBill.setRDPeakHours(averageBillParameter(rdPeakHours, billSize));
        averageBill.setRDOffHours(averageBillParameter(rdOffHours, billSize));
        averageBill.setRDFreeHours(averageBillParameter(rdFreeHours, billSize));
        averageBill.setRDContractedPower(averageBillParameter(rdContractedPower, billSize));
        averageBill.setRDReactivePower(averageBillParameter(rdReactivePower, billSize));
        averageBill.setAENormalHours(averageBillParameter(aeNormalHours, billSize));
        averageBill.setAEPeakHours(averageBillParameter(aePeakHours, billSize));
        averageBill.setAEOffHours(averageBillParameter(aeOffHours, billSize));
        averageBill.setAEFreeHours(averageBillParameter(aeFreeHours, billSize));

        return averageBill;
    }

    private BillParameterDto averageBillParameter(BillParameterDto parameterDto, int billSize) {
        parameterDto.setTotalTariffCost(parameterDto.getTotalTariffCost() / billSize);
        parameterDto.setTariffPrice(parameterDto.getTariffPrice() / billSize);
        parameterDto.setCost(parameterDto.getCost() / billSize);
        parameterDto.setConsumption(parameterDto.getConsumption() / billSize);
        return parameterDto;
    }

    private BillParameterDto mergeBillParam(BillParameterDto src, BillParameterDto dest) {
        if (dest != null) {
            src.setConsumption((dest.getConsumption()) + src.getConsumption());
            src.setParamType(dest.getParamType());
            src.setTariffPrice((dest.getTariffPrice()) + src.getTariffPrice());
            src.setCost((dest.getCost()) + src.getCost());
            src.setTotalTariffCost((dest.getTotalTariffCost()) + src.getTotalTariffCost());
        }
        return src;
    }

    private List<Bill> filterByYear(String buildingId, Integer year) {
        Map<String, AttributeValue> exprAtrVals = new HashMap<>();
        exprAtrVals.put(":fromYear", new AttributeValue().withN(String.valueOf(year)));
        exprAtrVals.put(":buildingId", new AttributeValue().withS(buildingId));
        String filterExpression = "buildingId = :buildingId and  fromYear= :fromYear";
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression(filterExpression)
                .withExpressionAttributeValues(exprAtrVals);

        return mapper.scan(Bill.class, scanExpression);
    }

    private String getDateStr(Date fromDate) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
        return df.format(fromDate);
    }

    private BillDto convertBillToDto(Bill bill) throws NotFoundException {

        BillParameterDto aeFreeDto = null;
        if (!StringUtils.isEmpty(bill.getAEFreeHours())) {
            BillParameterInfo aeFree = this.billParameterService.findById(bill.getAEFreeHours());
            aeFreeDto = new BillParameterDto(aeFree);
        }

        BillParameterDto aeNormalDto = null;
        if (!StringUtils.isEmpty(bill.getAENormalHours())) {
            BillParameterInfo aeNormal = this.billParameterService.findById(bill.getAENormalHours());
            aeNormalDto = new BillParameterDto(aeNormal);
        }

        BillParameterDto aeOffDto = null;
        if (!StringUtils.isEmpty(bill.getAEOffHours())) {
            BillParameterInfo aeOff = this.billParameterService.findById(bill.getAEOffHours());
            aeOffDto = new BillParameterDto(aeOff);
        }
        BillParameterDto aePeakDto = null;
        if (!StringUtils.isEmpty(bill.getAEPeakHours())) {
            BillParameterInfo aePeak = this.billParameterService.findById(bill.getAEPeakHours());
            aePeakDto = new BillParameterDto(aePeak);
        }
        BillParameterDto rdContractedDto = null;
        if (!StringUtils.isEmpty(bill.getRDContractedPower())) {
            BillParameterInfo rdContracted = this.billParameterService.findById(bill.getRDContractedPower());
            rdContractedDto = new BillParameterDto(rdContracted);
        }
        BillParameterDto rdPeakDto = null;
        if (!StringUtils.isEmpty(bill.getRDPeakHours())) {
            BillParameterInfo rdPeak = this.billParameterService.findById(bill.getRDPeakHours());
            rdPeakDto = new BillParameterDto(rdPeak);
        }

        BillParameterDto rdPowerPeakDto = null;
        if (!StringUtils.isEmpty(bill.getRDPowerPeakHours())) {
            BillParameterInfo rdPowerPeak = this.billParameterService.findById(bill.getRDPowerPeakHours());
            rdPowerPeakDto = new BillParameterDto(rdPowerPeak);
        }

        BillParameterDto rdFreeDto = null;
        if (!StringUtils.isEmpty(bill.getRDFreeHours())) {
            BillParameterInfo rdFree = this.billParameterService.findById(bill.getRDFreeHours());
            rdFreeDto = new BillParameterDto(rdFree);
        }
        BillParameterDto rdOffDto = null;
        if (!StringUtils.isEmpty(bill.getRDOffHours())) {
            BillParameterInfo rdOff = this.billParameterService.findById(bill.getRDOffHours());
            rdOffDto = new BillParameterDto(rdOff);
        }
        BillParameterDto rdNormalDto = null;
        if (!StringUtils.isEmpty(bill.getRDNormalHours())) {
            BillParameterInfo rdNormal = this.billParameterService.findById(bill.getRDNormalHours());
            rdNormalDto = new BillParameterDto(rdNormal);
        }
        BillParameterInfo rdReactive;
        BillParameterDto rdReactiveDto = null;
        if (bill.getRDReactivePower() != null) {
            rdReactive = this.billParameterService.findById(bill.getRDReactivePower());
            rdReactiveDto = new BillParameterDto(rdReactive);
        }

        BillDto billDto = new BillDto(bill.getBuildingId(),
                bill.getElectricityCounterCode(),
                bill.getCompanyTaxNumber(),
                bill.getAddress(),
                bill.getFromDate(),
                bill.getToDate(),
                bill.getFromYear(),
                bill.getFromMonth(),
                bill.getTotalPayable(),
                bill.getActiveEnergyCost(),
                bill.getProducedCO2(),
                bill.getPowerDemandCost(),
                bill.getAverageDailyConsumption(),
                bill.getTotalMonthlyConsumption(),
                aeFreeDto,
                rdFreeDto,
                rdOffDto,
                aeOffDto,
                aeNormalDto,
                rdNormalDto,
                aePeakDto,
                rdPeakDto,
                rdPowerPeakDto,
                rdContractedDto,
                rdReactiveDto
        );
        return billDto;
    }

    private Float geXValue(BuildingDto building, List<BillDto> dtoList) {
        // BuildingDto building = this.buildingService.findById(buildingId);
        // List<BillDto> dtoList = getBillsOfYear(buildingId);
        float x = 0f;
        for (int i = 0; i < 12; i++) {
            // TODO is normalized equals with averageConsumption?
            if (dtoList.size() > i) {
                // if (dtoList.get(i).getTotalMonthlyConsumption() != null) {
                x += ((dtoList.get(i).getTotalMonthlyConsumption()) / building.getArea());
                //}
            }
        }
        // return (x) / 12f;
        // According to Mr.Kamarlouei statement
        return x;
    }

    @Override
    public void delete(String id) throws NotFoundException {
        logger.info("trying to delete bill number : " + id);
        Bill bill = findById(id);
        if (!StringUtils.isEmpty(bill.getRDReactivePower())) {
            this.billParameterService.delete(bill.getRDReactivePower());
        }
        if (!StringUtils.isEmpty(bill.getAEFreeHours())) {
            this.billParameterService.delete(bill.getAEFreeHours());
        }
        if (!StringUtils.isEmpty(bill.getAENormalHours())) {
            this.billParameterService.delete(bill.getAENormalHours());
        }
        if (!StringUtils.isEmpty(bill.getAEOffHours())) {
            this.billParameterService.delete(bill.getAEOffHours());
        }
        if (!StringUtils.isEmpty(bill.getAEPeakHours())) {
            this.billParameterService.delete(bill.getAEPeakHours());
        }
        if (!StringUtils.isEmpty(bill.getRDContractedPower())) {
            this.billParameterService.delete(bill.getRDContractedPower());
        }
        if (!StringUtils.isEmpty(bill.getRDFreeHours())) {
            this.billParameterService.delete(bill.getRDFreeHours());
        }
        if (!StringUtils.isEmpty(bill.getRDNormalHours())) {
            this.billParameterService.delete(bill.getRDNormalHours());
        }
        if (!StringUtils.isEmpty(bill.getRDOffHours())) {
            this.billParameterService.delete(bill.getRDOffHours());
        }
        if (!StringUtils.isEmpty(bill.getRDPeakHours())) {
            this.billParameterService.delete(bill.getRDPeakHours());
        }
        this.repository.delete(bill);
        logger.info("bill number : " + id + " was successfully deleted!");
    }
}
