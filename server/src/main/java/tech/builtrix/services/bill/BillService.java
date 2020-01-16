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
    private static String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static Float reference = 50f;

    static DynamoDBMapper mapper = new DynamoDBMapper(client);

    private final BillParameterService billParameterService;
    //private final BuildingService buildingService;


    @Autowired
    protected BillService(BillRepository repository,
                          BillParameterService billParameterService) {
        super(repository);
        this.billParameterService = billParameterService;
    }

    public Bill findById(String id) throws NotFoundException {
        Optional<Bill> opitonal = this.repository.findById(id);
        if (opitonal.isPresent()) {
            return opitonal.get();
        } else {
            throw new NotFoundException("bill", "id", id);
        }
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
        BillParameterInfo rdReactivePower = null;
        if (rdReactivePowerDto != null) {
            rdReactivePower = this.billParameterService.save(rdReactivePowerDto);
        }
        Bill bill = new Bill(billDto);
        bill.setAEPeakHours(aePeakHours.getId());
        bill.setAEOffHours(aeOffHours.getId());
        bill.setAEFreeHours(aeFreeHours.getId());
        bill.setAENormalHours(aeNormalHours.getId());
        bill.setRDContractedPower(rdContractedPower.getId());
        bill.setRDPeakHours(rdPeakHours.getId());
        if (rdReactivePower != null) {
            bill.setRDReactivePower(rdReactivePower.getId());
        }
        bill = this.repository.save(bill);
        return bill;
    }

    public void update(BillDto billDto) {
        save(billDto);
    }

    public List<Bill> filterByFromDateAndMonthAndBuilding(Integer year,
                                                          Integer month1,
                                                          Integer month2,
                                                          Integer month3,
                                                          String buildingId) {
        // String fromDateStr = getDateStr(fromDate);
        Map<String, AttributeValue> exprAtrVals = new HashMap<>();
        exprAtrVals.put(":year", new AttributeValue().withN(year.toString()));
        exprAtrVals.put(":buildingId", new AttributeValue().withS(buildingId));
        exprAtrVals.put(":month1", new AttributeValue().withN(month1.toString()));
        exprAtrVals.put(":month2", new AttributeValue().withN(month2.toString()));
        exprAtrVals.put(":month3", new AttributeValue().withN(month3.toString()));
        String filterExpression = "buildingId = :buildingId and fromYear = :year " +
                "and fromMonth in (:month1, :month2, :month3)";
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression(filterExpression)
                .withExpressionAttributeValues(exprAtrVals);

        return mapper.scan(Bill.class, scanExpression);
    }

    public List<Bill> filterByFromDateAndMonthAndBuilding(Date fromDate, int month, String buildingId) {
        String fromDateStr = getDateStr(fromDate);
        Map<String, AttributeValue> exprAtrVals = new HashMap<>();
        exprAtrVals.put(":from_date", new AttributeValue().withS(fromDateStr));
        exprAtrVals.put(":buildingId", new AttributeValue().withS(buildingId));
        exprAtrVals.put(":month", new AttributeValue().withN(String.valueOf(month)));
        String filterExpression = "buildingId = :buildingId and fromDate <= :from_date " +
                "and fromMonth = :month";
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression(filterExpression)
                .withExpressionAttributeValues(exprAtrVals);
        return mapper.scan(Bill.class, scanExpression);
    }

    public Bill getLastBill(String buildingId) throws NotFoundException {
        //TODO find a way for fetching data order by fromDate
        Map<String, AttributeValue> exprAtrVals = new HashMap<>();
        exprAtrVals.put(":buildingId", new AttributeValue().withS(buildingId));
        String filterExpression = "buildingId = :buildingId ";
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression(filterExpression)
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
        String filterExpression = "buildingId = :buildingId " +
                "and fromMonth = :fromMonth and fromYear = :fromYear";
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression(filterExpression)
                .withExpressionAttributeValues(exprAtrVals);
        PaginatedScanList<Bill> scan = mapper.scan(Bill.class, scanExpression);
        if (!CollectionUtils.isEmpty(scan)) {
            Bill bill = scan.get(0);
            allBillInfo = convertBillToDto(bill);
        }
        return allBillInfo;
    }

    public Float getBEScore(BuildingDto building, List<BillDto> dtoList) throws NotFoundException {
        //TODO add reference field to building entity
        /*user enters reference if not we consider 50*/
        float GOD = 0.1f * reference; //5 kWh/m2/year
        Float x = geXValue(building, dtoList);
        Float beScore = (GOD / (x > 0 ? x : 0.5f)) * 100;
        return beScore;
    }

    public static Float getReference(String buildingId) {
        return reference;
    }

    public List<BillDto> getBillsOfYear(String buildingId, Integer year) throws NotFoundException {
        List<Bill> bills;
        List<BillDto> billDtos = new ArrayList<>();
        bills = filterByYear(buildingId, year);
        for (Bill bill : bills) {
            billDtos.add(convertBillToDto(bill));
        }
        return billDtos;
    }

    //----------------------------------------- Private Methods ---------------------------------------------

    private List<Bill> filterByYear(String buildingId, Integer year) {
        Map<String, AttributeValue> exprAtrVals = new HashMap<>();
        exprAtrVals.put(":fromYear", new AttributeValue().withN(String.valueOf(year)));
        exprAtrVals.put(":buildingId", new AttributeValue().withS(buildingId));
        String filterExpression = "buildingId = :buildingId and  fromYear= :fromYear";
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression(filterExpression)
                .withExpressionAttributeValues(exprAtrVals);

        return mapper.scan(Bill.class, scanExpression);
    }

    private String getDateStr(Date fromDate) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
        return df.format(fromDate);
    }

    private BillDto convertBillToDto(Bill bill) throws NotFoundException {
        BillParameterInfo aeFree = this.billParameterService.findById(bill.getAEFreeHours());
        BillParameterDto aeFreeDto = new BillParameterDto(aeFree);
        BillParameterInfo aeNormal = this.billParameterService.findById(bill.getAENormalHours());
        BillParameterDto aeNormalDto = new BillParameterDto(aeNormal);
        BillParameterInfo aeOff = this.billParameterService.findById(bill.getAEOffHours());
        BillParameterDto aeOffDto = new BillParameterDto(aeOff);
        BillParameterInfo aePeak = this.billParameterService.findById(bill.getAEPeakHours());
        BillParameterDto aePeakDto = new BillParameterDto(aePeak);
        BillParameterInfo rdContracted = this.billParameterService.findById(bill.getRDContractedPower());
        BillParameterDto rdContractedDto = new BillParameterDto(rdContracted);
        BillParameterInfo rdPeak = this.billParameterService.findById(bill.getRDPeakHours());
        BillParameterDto rdPeakDto = new BillParameterDto(rdPeak);
        BillParameterInfo rdReactive = null;
        BillParameterDto rdReactiveDto = null;
        if (bill.getRDReactivePower() != null) {
            rdReactive = this.billParameterService.findById(bill.getRDReactivePower());
            rdReactiveDto = new BillParameterDto(rdReactive);
        }

        BillDto billDto = new BillDto(bill.getBuildingId(),
                bill.getAddress(),
                bill.getFromDate(),
                bill.getFromYear(),
                bill.getFromMonth(),
                bill.getFromDate(),
                bill.getTotalPayable(),
                bill.getActiveEnergyCost(),
                bill.getProducedCO2(),
                bill.getPowerDemandCost(),
                bill.getAverageDailyConsumption(),
                bill.getTotalMonthlyConsumption(),
                aeOffDto,
                aeFreeDto,
                aeNormalDto,
                aePeakDto,
                rdPeakDto,
                rdContractedDto,
                rdReactiveDto
        );
        return billDto;
    }

    private Float geXValue(BuildingDto building, List<BillDto> dtoList) throws NotFoundException {
        //BuildingDto building = this.buildingService.findById(buildingId);
        // List<BillDto> dtoList = getBillsOfYear(buildingId);
        float x = 0f;
        for (int i = 0; i < 12; i++) {
            //TODO is normalized equals with averageConsumption?
            if (dtoList.size() > i) {
                if (dtoList.get(i).getTotalMonthlyConsumption() != null) {
                    x += ((dtoList.get(i).getTotalMonthlyConsumption()) / building.getArea());
                }
            }
        }
        return (x) / 12f;
    }

    public static void main(String[] args) throws ParseException {
        BillService billService = new BillService(null, null);
        // billService.filterByFromDateAndMonthAndBuilding(new Date(), 1, 2, 4, "999999");
        billService.filterByFromDateAndMonthAndBuilding(2019, 1, 2, 4, "999999");
    }
}
