package tech.builtrix.web.dtos.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.Objects;

@AllArgsConstructor
@JsonInclude
@Data
@NoArgsConstructor
public class DailyMatrix implements Comparable {
    DayOfWeek dayOfWeek;
    Month month;
    float consumption;

    @Override
    public int compareTo(Object o) {
        DailyMatrix o1 = (DailyMatrix) o;
        if (o.equals(this)) {
            return 0;
        } else {

            if (o1.getMonth().equals(this.month)) {
                return this.dayOfWeek.compareTo(o1.getDayOfWeek());
            } else {
                return this.getMonth().compareTo(o1.getMonth());
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DailyMatrix)) return false;
        DailyMatrix that = (DailyMatrix) o;
        return getDayOfWeek() == that.getDayOfWeek() &&
                getMonth() == that.getMonth();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDayOfWeek(), getMonth());
    }

    @Override
    public String toString() {
        return "[" + month +
                ", " + dayOfWeek +
                "," + consumption +
                ']';
    }
}
