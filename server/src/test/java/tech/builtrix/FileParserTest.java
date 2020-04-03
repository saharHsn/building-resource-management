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
            // String document = "2017-DEC-2018-JAN.pdf";

            // String buildingId = "8a199ea5-7c6a-4e80-8658-7ad2c53e69bf";
            // String buildingId = "4f9e5bc1-471d-4b37-87ca-82f803898bb6"; // Parede
            String buildingId = "9d94dd4d-b789-4717-bdee-517a8de8ca6e"; // Franklin
            deleteAllBuildingsBills(buildingId);
            parseFiles(buildingId);

        } catch (Exception e) {
            // logger.error();
            System.out.println("Error : " + e.getMessage());
        }
    }

    private void parseFiles(String buildingId) {
        // String bucket = "metrics-bills-test";
        // String bucket = "parede-building";
        String bucket = "franklin-building";
        //metrics-bills-test
        /*List<String> documents = Arrays.asList(
                "11190000169528.pdf",
                "11190000258792.pdf",
                "11190000316582.pdf",
                "11190000368992.pdf",
                "11190000368992.pdf",
                "11190000426568.pdf",
                "11190000473570.pdf",
                "11190000522534.pdf",
                "11190000568559.pdf",
                "11200000029587.pdf",
                "11200000032961.pdf",
                "11200000103798.pdf");*/
        //parede-building
      /*  List<String> documents = Arrays.asList(
                "11190000227592.pdf",
                "11190000431112.pdf",
                "11200000004975.pdf",
                "11190000227895.pdf",
                "11190000477407.pdf",
                "11200000056115.pdf",
                "11190000277080.pdf",
                "11190000525194.pdf",
                "11200000111731.pdf",
                "11190000322327.pdf",
                "11190000574221.pdf",
                "11190000379214.pdf",
                "11200000004974.pdf");
*/
        //franklin-building
        List<String> documents = Arrays.asList(
                "11190000173939.pdf",
                "11190000431226.pdf",
                "11200000056552.pdf",
                "11190000220218.pdf",
                "11190000477527.pdf",
                "11200000110504.pdf",
                "11190000277009.pdf",
                "11190000525021.pdf",
                "11190000313629.pdf",
                "11190000573173.pdf",
                "11190000379175.pdf",
                "11200000001443.pdf"
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
