package tech.builtrix.utils;

import com.amazonaws.services.dynamodbv2.datamodeling.marshallers.DateToStringMarshaller;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created By sahar at 12/3/19
 */
public class DateUtil {
    private static String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December"};

    public static Date getDateFromStr(String dateStr, String format) throws ParseException {
        DateFormat pdFormatter = new SimpleDateFormat(format);
        return pdFormatter.parse(dateStr);
    }

    public static Date fromLong(Long value) {
        if (value == null)
            return null;
        return new Date(value);
    }

    public static Date fromYearMonthDay(YearMonthDate yearMonthDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(yearMonthDate.getYear(), yearMonthDate.getMonth(), yearMonthDate.getDate());
        return calendar.getTime();
    }

    public static int getMonth(Date date) {
        int month;
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        month = gregorianCalendar.get(Calendar.MONTH);
        month = month + 1;
        return month;
    }

    public static int getWeekDay(Date date) {
        int weekDay;
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        weekDay = gregorianCalendar.get(Calendar.DAY_OF_WEEK);
        return weekDay;
    }

    public static int getNextNMonth(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, n);
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date nextMonthFirstDay = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date nextMonthLastDay = calendar.getTime();
        int month = calendar.get(Calendar.MONTH);
        return month + 1;
    }

    public static int getCurrentYear() {
        int year;
        GregorianCalendar date = new GregorianCalendar();
        year = date.get(Calendar.YEAR);
        return year;
    }

    public static Date convertDateStrToDate(String dateStr, String format) throws ParseException {
        DateFormat pdFormatter = new SimpleDateFormat(format);
        return pdFormatter.parse(dateStr);
    }

    public static Date setTimeForDate(Date date, Date time) {
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTime(time);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR));
        cal.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static boolean dateStrMatchesPattern(String dateStr, String pattern) {
        try {
            new SimpleDateFormat(pattern).parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static Date getDateFromPattern(String dateStr, String pattern) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            /*TimeZone tz = TimeZone.getTimeZone("UTC");
            simpleDateFormat.setTimeZone(tz);*/
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getDate(int year, int month, int day) {
        Calendar calendar = new GregorianCalendar(year, month - 1, day);
        return calendar.getTime();
    }

    public static Date removeTime(Date date) {
        date = removeTime(date, DateType.HOUR);
        date = removeTime(date, DateType.MINUTE);
        date = removeTime(date, DateType.SECOND);
        date = removeTime(date, DateType.MILLI_SECOND);
        return date;
    }

    public static Date removeTime(Date date, DateType dateType) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (dateType.equals(DateType.HOUR)) {
            cal.set(Calendar.HOUR_OF_DAY, 0);
        }
        if (dateType.equals(DateType.MINUTE)) {
            cal.set(Calendar.MINUTE, 0);
        }
        if (dateType.equals(DateType.SECOND)) {
            cal.set(Calendar.SECOND, 0);
        }
        if (dateType.equals(DateType.MILLI_SECOND))
            cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static void setDateField(Date date, Integer field, DateType dateType) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        switch (dateType) {
            case YEAR:
                cal.set(Calendar.YEAR, field);
                break;
            case MONTH:
                cal.set(Calendar.MONTH, field);
                break;
            case DAY:
                cal.set(Calendar.DAY_OF_MONTH, field);
                break;
            case HOUR:
                cal.set(Calendar.HOUR_OF_DAY, field);
                break;
            case MINUTE:
                cal.set(Calendar.MINUTE, field);
                break;
            case SECOND:
                cal.set(Calendar.SECOND, field);
                break;
            case MILLI_SECOND:
                cal.set(Calendar.MILLISECOND, 0);
                break;
        }
        cal.getTime();
    }

    public static Date increaseDate(Date date, int num, DateType dateType) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        switch (dateType) {
            case MILLI_SECOND:
                cal.add(Calendar.MILLISECOND, num);
                break;
            case SECOND:
                cal.add(Calendar.SECOND, num);
                break;
            case MINUTE:
                cal.add(Calendar.MINUTE, num);
                break;
            case HOUR:
                cal.add(Calendar.HOUR, num);
                break;
            case DAY:
                cal.add(Calendar.DAY_OF_MONTH, num);
                break;
            case MONTH:
                cal.add(Calendar.MONTH, num);
                break;
            case YEAR:
                cal.add(Calendar.YEAR, num);
                break;
        }
        return cal.getTime();
    }

    public static int getYear(Date time) {
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTime(time);
        return timeCalendar.get(Calendar.YEAR);
    }

    public static String getMonth(Integer monthNumber) {
        return monthNames[monthNumber - 1];
    }

    public static Date getCustomDate(Integer currentYear, int i) {
        Date date = new Date();
        DateUtil.setDateField(date, currentYear, DateUtil.DateType.YEAR);
        DateUtil.setDateField(date, i, DateUtil.DateType.MONTH);
        DateUtil.setDateField(date, 1, DateUtil.DateType.DAY);
        DateUtil.removeTime(date);
        return date;
    }

    public static Integer getNumOfDaysOfMonth(Integer year, int month) {
        YearMonth yearMonthObject = YearMonth.of(year, month);
        return yearMonthObject.lengthOfMonth();
    }

    public static int getCurrentMonth() {
        int month;
        GregorianCalendar date = new GregorianCalendar();
        month = date.get(Calendar.MONTH);
        return month;
    }

    public static int getDayOfMonth(Date date) {
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTime(date);
        return timeCalendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getNumOfDaysOfMonth(int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else if (month == 2) {
            return 29;
        } else
            return 30;
    }

    public enum DateType {
        MILLI_SECOND, SECOND, MINUTE, HOUR, DAY, MONTH, YEAR
    }

    public static class YearMonthDate {

        private int year;
        private int month;
        private int date;

        public YearMonthDate(int year, int month, int date) {
            this.year = year;
            this.month = month;
            this.date = date;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public String toString() {
            return getYear() + "/" + getMonth() + "/" + getDate();
        }
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        // isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = isoFormat.parse("2010-05-23T09:01:02");
        AttributeValue start = DateToStringMarshaller.instance().marshall(date);
        System.out.println(date);
       /* DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        //Date string with offset information
        String dateString = 2020 + "-" + "12" + "-" + DateUtil.getNumOfDaysOfMonth(2020, 12) + "T23:59:59";

        //Instance with given offset
        OffsetDateTime odtInstanceAtOffset = OffsetDateTime.parse(dateString, DATE_TIME_FORMATTER);

        //Instance in UTC
        OffsetDateTime odtInstanceAtUTC = odtInstanceAtOffset.withOffsetSameInstant(ZoneOffset.UTC);

        //Formatting to string
        String dateStringInUTC = odtInstanceAtUTC.format(DATE_TIME_FORMATTER);

        System.out.println(odtInstanceAtOffset);
        System.out.println(odtInstanceAtUTC);
        System.out.println(dateStringInUTC);

        //Convert OffsetDateTime to instant which is in UTC
        System.out.println(odtInstanceAtOffset.toInstant());*/
    }

}
