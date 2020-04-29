package tech.builtrix;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tech.builtrix.eventbus.MessageManager;
import tech.builtrix.events.FileUploaded;
import tech.builtrix.exceptions.BillParseException;
import tech.builtrix.models.building.enums.BillType;
import tech.builtrix.parseEngine.PdfParser;
import tech.builtrix.repositories.FileUploader;
import tech.builtrix.services.bill.BillParser;
import tech.builtrix.services.bill.BillService;
import tech.builtrix.services.building.BuildingService;
import tech.builtrix.services.historical.HourlyDailyService;
import tech.builtrix.utils.FileUtil;
import tech.builtrix.web.dtos.bill.BuildingDto;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By sahar at 28/4/20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class UpdateHorlyMonthlyInfo {
    @Autowired
    private BillParser billParser;
    @Autowired
    private PdfParser pdfParser;
    @Autowired
    private BillService billService;
    @Autowired
    private BuildingService buildingService;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private FileUploader fileUploader;
    @Autowired
    private HourlyDailyService hourlyDailyService;

    @Before
    public void setup() throws Exception {
    }

    @After
    public void onFinish() throws Exception {
    }

    @Test
    public void uploadAndParseBills() throws IOException, ParseException, BillParseException {
        String bucketName = "metrics-building";
        List<BuildingDto> dtoList = makeDtoList();
        for (BuildingDto buildingDto : dtoList) {
            String buildingId = buildingDto.getId();
            List<String> fileNames = this.buildingService.uploadFile(bucketName, buildingId, buildingDto.getElectricityBill(), BillType.Electricity);
            if (!StringUtils.isEmpty(fileNames)) {
                FileUploaded fileUploaded = new FileUploaded(buildingId, fileNames, bucketName);
                if (buildingDto.getElectricityBill() != null) {
                    // publish add fileUploaded event
                    this.buildingService.parseBillFiles(fileUploaded);
                }
            }
        }
    }

    @Test
    public void persistHourlyInfo() throws IOException {
        List<BuildingDto> dtoList = makeDtoList();
        for (BuildingDto buildingDto : dtoList) {
            this.hourlyDailyService.parseExcelData(buildingDto.getHourlyFile(), buildingDto.getId());
        }
    }


    private List<BuildingDto> makeDtoList() throws IOException {
        List<BuildingDto> dtoList = new ArrayList<>();
        /*BuildingDto demo = new BuildingDto();
        demo.setId("fae0c9a2-ef89-477a-a073-a9e704e5ccb3");
        demo.setElectricityBill(FileUtil.createMultiPartFile());*/
        BuildingDto demo = new BuildingDto();
        demo.setId("fae0c9a2-ef89-477a-a073-a9e704e5ccb3");
       /* MultipartFile dbill = FileUtil.createMultiPartFile("11200000153336.pdf", "/Users/sahar/IdeaProjects/builtrix-metrics-new/server/src/test/java/tech/builtrix/billFiles/franklin/11200000153336.pdf");
        demo.setElectricityBill(dbill);*/
        MultipartFile dhourly = FileUtil.createMultiPartFile("Hourly Mat _ Demo _ General _ 18042020.xlsx",
                "/Users/sahar/IdeaProjects/builtrix-metrics-new/server/src/test/java/tech/builtrix/billFiles/demo/Hourly Mat _ Demo _ General _ 18042020.xlsx");
        demo.setHourlyFile("/Users/sahar/IdeaProjects/builtrix-metrics-new/server/src/test/java/tech/builtrix/billFiles/demo/Hourly Mat _ Demo _ General _ 18042020.xlsx");
        dtoList.add(demo);
        dtoList.add(demo);

        BuildingDto franklin = new BuildingDto();
        franklin.setId("9d94dd4d-b789-4717-bdee-517a8de8ca6e");
        MultipartFile fbill = FileUtil.createMultiPartFile("11200000153336.pdf", "/Users/sahar/IdeaProjects/builtrix-metrics-new/server/src/test/java/tech/builtrix/billFiles/franklin/11200000153336.pdf");
        franklin.setElectricityBill(fbill);
        MultipartFile fhourly = FileUtil.createMultiPartFile("Hourly Mat _ FL _ General _ 18042020.xlsx",
                "/Users/sahar/IdeaProjects/builtrix-metrics-new/server/src/test/java/tech/builtrix/billFiles/franklin/Hourly Mat _ FL _ General _ 18042020.xlsx");
        franklin.setHourlyFile("/Users/sahar/IdeaProjects/builtrix-metrics-new/server/src/test/java/tech/builtrix/billFiles/franklin/Hourly Mat _ FL _ General _ 18042020.xlsx");
        dtoList.add(franklin);

        BuildingDto parede = new BuildingDto();
        parede.setId("4f9e5bc1-471d-4b37-87ca-82f803898bb6");
        MultipartFile pbill = FileUtil.createMultiPartFile("11200000154090.pdf", "/Users/sahar/IdeaProjects/builtrix-metrics-new/server/src/test/java/tech/builtrix/billFiles/parede/11200000154090.pdf");
        parede.setElectricityBill(pbill);
        MultipartFile phourly = FileUtil.createMultiPartFile("Hourly Mat _ PA _ General _ 18042020.xlsx",
                "/Users/sahar/IdeaProjects/builtrix-metrics-new/server/src/test/java/tech/builtrix/billFiles/parede/Hourly Mat _ PA _ General _ 18042020.xlsx");
        parede.setHourlyFile("/Users/sahar/IdeaProjects/builtrix-metrics-new/server/src/test/java/tech/builtrix/billFiles/parede/Hourly Mat _ PA _ General _ 18042020.xlsx");
        dtoList.add(parede);

        return dtoList;
    }
                /*{
  "active": 1,
  "area": 1500,
  "BuildingAge": "MORE_THAN_15",
  "BuildingUsage": "HOTEL_OR_SIMILAR_BUILDINGS",
  "EnergyCertificate": "D",
  "id": "fae0c9a2-ef89-477a-a073-a9e704e5ccb3",
  "name": "Lisbon Office",
  "numberOfPeople": 124,
  "Owner": "9289da95-0b77-40de-a01c-7f19fb241156", : Pedro (demo user)
  "postalAddress": "Lisbon",
  "postalCode": "8888-888"
}*/
    /*

 {
  "active": 1,
  "area": 350,
  "BuildingAge": "BETWEEN_5_TO_10_YEARS",
  "BuildingUsage": "OFFICE_BUILDING_OR_CO_WORK_SPACE",
  "EnergyCertificate": "B",
  "id": "9d94dd4d-b789-4717-bdee-517a8de8ca6e",
  "name": "Franklim Lamas (FL)",
  "numberOfPeople": 90,
  "Owner": "9289da95-0b63-40de-a01c-7f19fb241156", : Jao
  "postalAddress": " Emac - Empresa Municipal De Ambiente De Cascais, E.M., S.A",
  "postalCode": "2645-138"
}*/
            /*{
  "active": 1,
  "area": 800,
  "BuildingAge": "BETWEEN_10_TO_15_YEARS",
  "BuildingUsage": "OFFICE_BUILDING_OR_CO_WORK_SPACE",
  "EnergyCertificate": "C",
  "id": "4f9e5bc1-471d-4b37-87ca-82f803898bb6",
  "name": "Parede (PA)",
  "numberOfPeople": 170,
  "Owner": "9289da95-0b63-40de-a01c-7f19fb241157", : Tomas
  "postalAddress": "Emac - Empresa Municipal De Ambiente De Cascais, E.M., S.A",
  "postalCode": "2645-138"
}*/


}
