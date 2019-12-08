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
            String document = "2018_apr_may.pdf";
            String bucket = "textract-console-us-east-2-64c71e37-898b-403b-80b7-942d95a9cf48";
            this.billParser.parseBill("101010110", bucket, document);
            /*TExtractDto tExtractDto = this.pdfParser.parseFile(bucket, document);
            System.out.println("key value result : " + tExtractDto.getKeyValueResult());
            System.out.println("table result : " + tExtractDto.getTablesResult());*/
        } catch (Exception e) {
            // logger.error();
            System.out.println("Error : " + e.getMessage());
        }
    }
}
