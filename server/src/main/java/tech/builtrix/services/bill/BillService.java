package tech.builtrix.services.bill;

/**
 * Created By sahar at 12/2/19
 */

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.builtrix.base.GenericCrudServiceBase;
import tech.builtrix.dtos.bill.BillDto;
import tech.builtrix.dtos.bill.BillParameterDto;
import tech.builtrix.models.bill.Bill;
import tech.builtrix.models.bill.BillParameterInfo;
import tech.builtrix.repositories.bill.BillRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class BillService extends GenericCrudServiceBase<Bill, BillRepository> {
    private static String billTableName = Bill.class.getAnnotation(DynamoDBTable.class).tableName();
    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    DynamoDB dynamoDB = new DynamoDB(client);
    static DynamoDBMapper mapper = new DynamoDBMapper(client);
    private DynamoDBTypeConverter<AttributeValue, Object> converter;

    private final BillParameterService billParameterService;
    private String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

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

    private List<Bill> filterByFromDateAndMonthAndBuilding(Date fromDate,
                                                           Integer month1,
                                                           Integer month2,
                                                           Integer month3,
                                                           String buildingId) {
        Map<String, AttributeValue> exprAtrVals = new HashMap<>();
        SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
        String fromDateStr = df.format(fromDate);
        exprAtrVals.put(":from_date", new AttributeValue().withS(fromDateStr));
        exprAtrVals.put(":buildingId", new AttributeValue().withS(buildingId));
        exprAtrVals.put(":month1", new AttributeValue().withN(month1.toString()));
        exprAtrVals.put(":month2", new AttributeValue().withN(month2.toString()));
        exprAtrVals.put(":month3", new AttributeValue().withN(month3.toString()));
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("buildingId = :buildingId and fromDate <= :from_date " +
                        "and fromMonth in (:month1, :month2, :month3)")
                .withExpressionAttributeValues(exprAtrVals);

        List<Bill> scanResult = mapper.scan(Bill.class, scanExpression);
        return scanResult;
    }

    /*  public BillDateCostDto getBillFromMap(Map<String, AttributeValue> billItem) throws ParseException {
          BillDateCostDto dto = new BillDateCostDto();
          dto.setTotalPayable(Float.valueOf(billItem.get("totalPayable").getN()));
          String fromDate = billItem.get("fromDate").getS();
          dto.setFromDate(DateUtil.convertDateStrToDate(fromDate, DATE_PATTERN));
          dto.setId(billItem.get("id").getS());
          dto.setFromMonth(Integer.valueOf(billItem.get("fromMonth").getN()));
          dto.setFromYear(Integer.valueOf(billItem.get("fromYear").getN()));
          return dto;
      }
  */
   /*public List<BillDateCostDto> filterByFromDateAndMonthAndBuilding(Date fromDate,
                                                                     Integer month1,
                                                                     Integer month2,
                                                                     Integer month3,
                                                                     String buildingId) throws ParseException {
        //2018-04-17T19:30:00.000Z
        //"attr1 = :val1 and attr2 = :val2 and (contains(attr3, :val3a) or contains(attr3, :val3b))"
        Map<String, AttributeValue> exprAtrVals = new HashMap<>();
        SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
        String fromDateStr = df.format(fromDate);
        exprAtrVals.put(":from_date", new AttributeValue().withS(fromDateStr));
        exprAtrVals.put(":buildingId", new AttributeValue().withS(buildingId));
        exprAtrVals.put(":month1", new AttributeValue().withN(month1.toString()));
        exprAtrVals.put(":month2", new AttributeValue().withN(month2.toString()));
        exprAtrVals.put(":month3", new AttributeValue().withN(month3.toString()));

        ScanRequest scanRequest = new ScanRequest()
                .withTableName(billTableName)
                .withProjectionExpression("id, buildingId, fromDate, fromYear, fromMonth, totalPayable")
                .withFilterExpression("buildingId = :buildingId and fromDate <= :from_date " +
                        "and fromMonth in (:month1, :month2, :month3)")
                .withExpressionAttributeValues(exprAtrVals);
        ScanResult result = client.scan(scanRequest);
        List<BillDateCostDto> dtoList = new ArrayList<>();

        for (Map<String, AttributeValue> item : result.getItems()) {
            dtoList.add(getBillFromMap(item));
        }
        return dtoList;
    }*/

    public void updateItem() {

        Table table = dynamoDB.getTable(billTableName);

        String id = "0bdd7f99-f3af-4cec-a2e2-269a65de9df1";

        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("id", id)
                .withUpdateExpression("set year = :year, month = :month")
                .withValueMap(new ValueMap().withNumber(":year", 2018)
                        .withNumber("month", 4))
                .withReturnValues(ReturnValue.UPDATED_NEW);

        try {
            System.out.println("Updating the item...");
            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
            System.out.println("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws ParseException {
        BillService billService = new BillService(null, null);
        // billService.filterByFromDateAndMonthAndBuilding(new Date(), 1, 2, 4, "999999");
        billService.filterByFromDateAndMonthAndBuilding(new Date(), 1, 2, 4, "999999");
    }

}
