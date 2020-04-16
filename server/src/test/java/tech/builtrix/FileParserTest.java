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
            String buildingId = "fae0c9a2-ef89-477a-a073-a9e704e5ccb3";
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
                "fae0c9a2-ef89-477a-a073-a9e704e5ccb3-2019-1.pdf",
                "fae0c9a2-ef89-477a-a073-a9e704e5ccb3-2019-10.pdf",
                "fae0c9a2-ef89-477a-a073-a9e704e5ccb3-2019-11.pdf",
                "fae0c9a2-ef89-477a-a073-a9e704e5ccb3-2019-12.pdf",
                "fae0c9a2-ef89-477a-a073-a9e704e5ccb3-2019-2.pdf",
                "fae0c9a2-ef89-477a-a073-a9e704e5ccb3-2019-3.pdf",
                "fae0c9a2-ef89-477a-a073-a9e704e5ccb3-2019-4.pdf",
                "fae0c9a2-ef89-477a-a073-a9e704e5ccb3-2019-5.pdf",
                "fae0c9a2-ef89-477a-a073-a9e704e5ccb3-2019-6.pdf",
                "fae0c9a2-ef89-477a-a073-a9e704e5ccb3-2019-7.pdf",
                "fae0c9a2-ef89-477a-a073-a9e704e5ccb3-2019-8.pdf",
                "fae0c9a2-ef89-477a-a073-a9e704e5ccb3-2019-9.pdf",
                "fae0c9a2-ef89-477a-a073-a9e704e5ccb3-2020-1.pdf",
                "fae0c9a2-ef89-477a-a073-a9e704e5ccb3-2020-2.pdf",
                "fae0c9a2-ef89-477a-a073-a9e704e5ccb3-2020-3.pdf"
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

    private void deleteAllBuildingsBills(String buildingId) throws NotFoundException {
        List<Bill> buildingsBills = this.billService.findByBuilding(buildingId);
        for (Bill bill : buildingsBills) {
            billService.delete(bill.getId());
        }
    }
}
