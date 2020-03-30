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
    private static Float reference = 50f;
    private final BillParameterService billParameterService;
    // private final BuildingService buildingService;

    @Autowired
    protected BillService(BillRepository repository, BillParameterService billParameterService) {
        super(repository);
        this.billParameterService = billParameterService;
    }

    public static Float getReference(String buildingId) {
        return reference;
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
        BillParameterDto rdReactivePowerDto = billDto.getRDReactivePower();
        BillParameterInfo rdReactivePower = null;
        if (rdReactivePowerDto != null) {
            rdReactivePower = this.billParameterService.save(rdReactivePowerDto);
        }
        Bill bill = new Bill(billDto);
        bill.setAEPeakHours(aePeakHours != null ? aePeakHours.getId() : null);
        bill.setAEOffHours(aeOffHours != null ? aeOffHours.getId() : null);
        bill.setAEFreeHours(aeFreeHours != null ? aeFreeHours.getId() : null);
        bill.setAENormalHours(aeNormalHours != null ? aeNormalHours.getId() : null);
        bill.setRDContractedPower(rdContractedPower != null ? rdContractedPower.getId() : null);
        bill.setRDPeakHours(rdPeakHours != null ? rdPeakHours.getId() : null);
        bill.setRDReactivePower(rdReactivePower != null ? rdReactivePower.getId() : null);
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
        BillDto allBillInfo = new BillDto();
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

    public Float getBEScore(BuildingDto building, List<BillDto> dtoList) throws NotFoundException {
        // TODO add reference field to building entity
        /* user enters reference if not we consider 50 */
        float GOD = 0.1f * reference; // 5 kWh/m2/year
        Float x = geXValue(building, dtoList);
        Float beScore = (GOD / (x > 0 ? x : 0.5f)) * 100;
        return beScore;
    }

    // ----------------------------------------- Private Methods
    // ---------------------------------------------

    public List<BillDto> getBillsOfYear(String buildingId, Integer year) throws NotFoundException {
        List<Bill> bills;
        List<BillDto> billDtos = new ArrayList<>();
        bills = filterByYear(buildingId, year);
        for (Bill bill : bills) {
            billDtos.add(convertBillToDto(bill));
        }
        return billDtos;
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
                rdContractedDto,
                rdReactiveDto
        );
        return billDto;
    }

    private Float geXValue(BuildingDto building, List<BillDto> dtoList) throws NotFoundException {
        // BuildingDto building = this.buildingService.findById(buildingId);
        // List<BillDto> dtoList = getBillsOfYear(buildingId);
        float x = 0f;
        for (int i = 0; i < 12; i++) {
            // TODO is normalized equals with averageConsumption?
            if (dtoList.size() > i) {
                if (dtoList.get(i).getTotalMonthlyConsumption() != null) {
                    x += ((dtoList.get(i).getTotalMonthlyConsumption()) / building.getArea());
                }
            }
        }
        // return (x) / 12f;
        // According to Mr.Kamarlouei statement
        return x;
    }

    @Override
    public void delete(String id) throws NotFoundException {
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
    }
}
