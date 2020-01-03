package tech.builtrix;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tech.builtrix.parseEngine.PdfParser;
import tech.builtrix.services.bill.BillParser;
import tech.builtrix.services.bill.BillService;
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

            String bucket = "metrics-building-0023499e-0bd1-44bb-b3d5-15da81f0ef12";
            /* "2017-DEC-2018-JAN.pdf",
                    "2018-APR-MAY.pdf",
                    "2018-AUG-SEP.pdf",
                     "2018-DEC-2019-JAN.pdf",
                      "2018-FEB-MAR.pdf",
                       "2018-JAN-FEB.pdf",
                    "2018-JUL-Aug.pdf",
                       "2018-JUN-JUL.pdf",
                    "2018-MAR-APR.pdf",
                    "2018-MAY-JUN.pdf",
                    "2018-OCT-NOV.pdf",
                    "2018-SEP-OCT.pdf",
                    "2019-FEB-MAR.pdf",
                    "2019-JAN-FEB.pdf",
                    "2018-JUN-JUL.pdf",
                    "2018-MAR-APR.pdf",
                    "2018-SEP-OCT.pdf"*/

                 /* ,
                    ,
                    */
            List<String> documents = Arrays.asList("2019-FEB-MAR.pdf");
            for (String document : documents) {
                BillDto billDto;
                try {
                    billDto = this.billParser.parseBill("0023499e-0bd1-44bb-b3d5-15da81f0ef12", bucket, document);
                    billService.save(billDto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            /*TExtractDto tExtractDto = this.pdfParser.parseFile(bucket, document);
            System.out.println("key value result : " + tExtractDto.getKeyValueResult());
            System.out.println("table result : " + tExtractDto.getTablesResult());*/
        } catch (Exception e) {
            // logger.error();
            System.out.println("Error : " + e.getMessage());
        }
    }
}
