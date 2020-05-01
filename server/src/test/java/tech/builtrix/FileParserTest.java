package tech.builtrix;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.models.bill.Bill;
import tech.builtrix.parseEngine.PdfParser;
import tech.builtrix.services.bill.BillParser;
import tech.builtrix.services.bill.BillService;
import tech.builtrix.services.building.BuildingService;
import tech.builtrix.web.dtos.bill.BillDto;

import java.util.Arrays;
import java.util.List;

/**
 * Created By sahar at 12/2/19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class FileParserTest {
    @Autowired
    private BillParser billParser;
    @Autowired
    private PdfParser pdfParser;
    @Autowired
    private BillService billService;
    @Autowired
    private BuildingService buildingService;

    public FileParserTest() {
    }

    @Before
    public void setup() throws Exception {
    }

    @After
    public void onFinish() throws Exception {
    }

    @Test
    public void fileExtractorTest() {
        try {
            // demo : "id": "fae0c9a2-ef89-477a-a073-a9e704e5ccb3",
            // franklin "id": "9d94dd4d-b789-4717-bdee-517a8de8ca6e",
            // parede "id": "4f9e5bc1-471d-4b37-87ca-82f803898bb6",
            String buildingId = "9d94dd4d-b789-4717-bdee-517a8de8ca6e";
            // deleteAllBuildingsBills(buildingId);
            parseFiles(buildingId);

        } catch (Exception e) {
            // logger.error();
            System.out.println("Error : " + e.getMessage());
        }
    }

    private void parseFiles(String buildingId) {
        String bucket = "metrics-building";
        List<String> documents = Arrays.asList(
                "11190000431226.pdf", "11190000573173.pdf",
                "11190000477527.pdf", "11200000001443.pdf",
                "11190000525021.pdf"
        );
        for (String document : documents) {
            BillDto billDto;
            try {
                billDto = this.billParser.parseBill(buildingId, bucket, document);
                billService.save(billDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void getBill() throws NotFoundException {
        Bill byId = this.billService.findById("ffedc515-4115-41db-b9d1-34e5ba461066");
        System.out.println(byId.getRDReactivePower());
    }

    private void deleteAllBuildingsBills(String buildingId) throws NotFoundException {
        List<Bill> buildingsBills = this.billService.findByBuilding(buildingId);
        for (Bill bill : buildingsBills) {
            billService.delete(bill.getId());
        }
    }

}
