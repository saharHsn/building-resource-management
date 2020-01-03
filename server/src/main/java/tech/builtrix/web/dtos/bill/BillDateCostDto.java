package tech.builtrix.web.dtos.bill;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Created By sahar at 12/8/19
 */
@JsonInclude
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillDateCostDto implements Serializable {
    private String id;
    private Date fromDate;
    private Float totalPayable;
    private Integer fromYear;
    private Integer fromMonth;

    /*public enum ColumnNames {
        ID("id"),
        FROM_DATE("creationTime"),
        COST("countOfUses"),
        FROM_YEAR("timeToLive"),
        FROM_MONTH("timeToIdle");
        private final String name;
        ColumnNames(final String name) {
            this.name = name;
        }
        public static String getName() {
            return name;
        }
    }*/

}
