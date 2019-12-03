package tech.builtrix.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created By sahar at 12/3/19
 */
public class DateUtil {
    public static Date getDateFromStr(String dateStr, String format) throws ParseException {
        DateFormat pdFormatter = new SimpleDateFormat(format);
        return pdFormatter.parse(dateStr);
    }
}
