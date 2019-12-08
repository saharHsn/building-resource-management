package tech.builtrix;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tech.builtrix.services.bill.BillService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {
    @Autowired
    BillService billService;

    @Test
    public void contextLoads() {
        //billService.filterByFromDateAndMonthAndBuilding(new Date(), 1, 2, 4, "999999");
    }

}
