package tech.builtrix.services.historical;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import tech.builtrix.models.historical.HistoricalConsumption;
import tech.builtrix.models.historical.enums.HourPeriod;
import tech.builtrix.models.historical.enums.Season;
import tech.builtrix.models.historical.enums.WeekDayRange;
import tech.builtrix.services.S3FileService;
import tech.builtrix.utils.DateUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static java.lang.System.out;

@Component
@Slf4j
public class HourlyDailyService {
    private final S3FileService s3FileService;
    private static final String HISTORICAL_ENERGY_CONSUMPTION_BUCKET = "historical-energy-consumption";
    //28-03-2020 18:00
    private static final String DATE_PATTERN = "dd-MM-yyyy HH:mm";
    //2/12/2020  10:00:00 AM
    private static final String DATE_PATTERN2 = "MM/dd/yy HH:mm";
    private static final String TIME_PATTERN = "HH:mm";
    private static final String TIME_PATTERN2 = "HH:mm:ss";
    private static float SUPER_VAZIO_COST_COEFFICIENT = 0.070750f;
    private static float VAZIO_NORMAL_COST_COEFFICIENT = 0.077610f;
    private static float PONTA_COST_COEFFICIENT = 0.089990f;
    private static float CHEIA_COST_COEFFICIENT = 0.089700f;

    private final HistoricalConsumptionService consumptionService;

    public HourlyDailyService(S3FileService s3FileService, HistoricalConsumptionService consumptionService) {
        this.s3FileService = s3FileService;
        this.consumptionService = consumptionService;
    }

    public void parseExcelData(String buildingId) throws IOException {
        // building-9d94dd4d-b789-4717-bdee-517a8de8ca6e.xlsx
        String fileName = "building-" + buildingId + ".xlsx";
        S3ObjectInputStream inputStream = this.s3FileService.downloadFile(HISTORICAL_ENERGY_CONSUMPTION_BUCKET, fileName);
        Files.copy(inputStream, Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
        try {
            FileInputStream file = new FileInputStream(new File(fileName));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowNum = 0;
            int dateNum = 0, consNum = 0;
            List<HistoricalConsumption> consumptionList = new ArrayList<>();
            for (Row row : sheet) {
                HistoricalConsumption consumption = new HistoricalConsumption(buildingId);
                out.println("Row Number : " + ++rowNum);
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if (cell.getCellTypeEnum() == CellType.STRING) {
                        String dateStr = cell.toString();
                        Date date;
                        String hourStr;
                        try {
                            date = new SimpleDateFormat(DATE_PATTERN).parse(String.valueOf(dateStr));
                            hourStr = new SimpleDateFormat(TIME_PATTERN).format(date);
                            float floatValueOfHour = getFloatValueOfHour(hourStr);
                            consumption.setHour(floatValueOfHour);
                            consumption.setReportDate(date);
                            // out.println("Date Number : " + ++dateNum);
                            continue;
                        } catch (ParseException e) {
                            // continue;
                        }
                        try {
                            /*Number1.5*/
                            float numericCellValue = Float.parseFloat(cell.getStringCellValue().replaceAll(",", "."));
                            consumption.setConsumption(numericCellValue);
                            // out.println("Cons Number : " + ++consNum);
                        } catch (Exception ignored) {
                        }
                    }
                }
                if (consumption.getReportDate() != null) {
                    consumptionList.add(consumption);
                }
            }
            file.close();
            int row = 0;
            for (HistoricalConsumption consumption : consumptionList) {
                //TODO check uniqueness of date field
                long l = System.currentTimeMillis();
                logger.info("Trying to persist historical " + ++row);
                categorizeConsumption(consumption);
                if (consumption.getHourPeriod().equals(HourPeriod.UNKNOWN)) {
                    out.println("UNKNOWN HOUR PERIOD!!!");
                }
                switch (consumption.getHourPeriod()) {
                    case Super_Vazio:
                        consumption.setCost(consumption.getConsumption() * SUPER_VAZIO_COST_COEFFICIENT);
                        break;
                    case Vazio_Normal:
                        consumption.setCost(consumption.getConsumption() * VAZIO_NORMAL_COST_COEFFICIENT);
                        break;
                    case Ponta:
                        consumption.setCost(consumption.getConsumption() * PONTA_COST_COEFFICIENT);
                        break;
                    case Cheia:
                        consumption.setCost(consumption.getConsumption() * CHEIA_COST_COEFFICIENT);
                        break;
                }
                this.consumptionService.save(consumption);
                logger.info("row " + row + " was saved successfully in : " + (System.currentTimeMillis() - l) + "milliSeconds");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

   /* public void copyDateData(){
        List<HistoricalConsumption> consumptions = consumptionService.findAll();
        for (HistoricalConsumption consumption : consumptions) {
            consumption.setReportDate(consumption.getDate());
            this.consumptionService.update(consumption);
        }
    }*/
        /*Super Vazio (SV) -->              	  Winter                                                Summer
                                Monday-Friday     2:00/6:00                                             2:00/6:00
                                Saturday		  2:00/6:00                                             2:00/6:00
                                Sunday			  2:00/6:00                                             2:00/6:00
        Vazio Normal (VN) -->
                                Monday-Friday     0:00/2:00 , 6:00/7:00                                 0:00/2:00 , 6:00/7:00
                                Saturday		  0:00/2:00 , 6:00/9:30 , 13:00/18:30 , 22:00/24:00     0:00/2:00 , 6:00/9:00 , 14:00/20:00 , 22:00/24:00
                                Sunday			  0:00/2:00 , 6:00/24:00                                0:00/2:00 , 6:00/24:00
        Ponta (P) -->
                                Monday-Friday     9:30/12:00 , 18:30/21                                 9:15/12:15
                                Saturday          -                                                     -
                                Sunday            -                                                     -

       Cheia (C) -->
                                 Monday-Friday    7:00/9:30 , 12:00/18:30 , 21:00/24:00	                7:00/9:15 , 12:15/24:00
                                 Saturday         9:30/13:00 , 18:30/22:00	                            9:00/14:00 , 20:00/22:00
                                 Sunday           -	                                                    -
        */

    private void categorizeConsumption(HistoricalConsumption consumption) {
        Season season = extractSeason(consumption.getReportDate());
        WeekDayRange weekDayRange = extractWeekDayRange(consumption.getReportDate());
        float hour = consumption.getHour();
        HourPeriod hourPeriod = HourPeriod.UNKNOWN;
        //2:00/6:00
        if ((hour >= 2 && hour < 6)) {
            hourPeriod = HourPeriod.Super_Vazio;
        } else {
            if (season != null) {
                if (season.equals(Season.WINTER)) {
                    if (weekDayRange != null) {
                        if (weekDayRange.equals(WeekDayRange.MONDAY_TO_FRIDAY)) {
                            //0:00/2:00 , 6:00/7:00
                            if ((hour >= 0 && hour < 2) || (hour >= 6 && hour < 7)) {
                                hourPeriod = HourPeriod.Vazio_Normal;
                            }
                            //9:30/12:00 , 18:30/21
                            if ((hour >= 9.5 && hour < 12) || (hour >= 18.5 && hour < 21)) {
                                hourPeriod = HourPeriod.Ponta;
                            }
                            //7:00/9:30 , 12:00/18:30 , 21:00/24:00
                            if ((hour >= 7 && hour < 9.5) || (hour >= 12 && hour < 18.5) || (hour >= 21 && hour < 24)) {
                                hourPeriod = HourPeriod.Cheia;
                            }
                        } else if (weekDayRange.equals(WeekDayRange.SATURDAY)) {
                            //0:00/2:00 , 6:00/9:30 , 13:00/18:30 , 22:00/24:00
                            if ((hour >= 0 && hour < 2) || (hour >= 6 && hour < 9.5) || (hour >= 13 && hour < 18.50) || (hour >= 22 && hour < 24)) {
                                hourPeriod = HourPeriod.Vazio_Normal;
                            }
                            //9:30/13:00 , 18:30/22:00
                            if ((hour >= 9.5 || hour < 13) || (hour >= 18.5 || hour < 22)) {
                                hourPeriod = HourPeriod.Cheia;
                            }

                        } else if (weekDayRange.equals(WeekDayRange.SUNDAY)) {
                            //0:00/2:00 , 6:00/24:00
                            if ((hour >= 0 && hour < 2) || (hour >= 6 && hour < 24)) {
                                hourPeriod = HourPeriod.Vazio_Normal;
                            }

                        }
                    }
                } else if (season.equals(Season.SUMMER)) {
                    if (weekDayRange != null) {
                        if (weekDayRange.equals(WeekDayRange.MONDAY_TO_FRIDAY)) {
                            //0:00/2:00 , 6:00/7:00
                            if ((hour >= 0 && hour < 2) || (hour >= 6 && hour < 7)) {
                                hourPeriod = HourPeriod.Vazio_Normal;
                            }
                            //9:15/12:15
                            if (hour >= 9.25 && hour < 12.25) {
                                hourPeriod = HourPeriod.Ponta;
                            }
                            //7:00/9:15 , 12:15/24:00
                            if ((hour >= 7 && hour < 9.25) || (hour >= 12.25 && hour < 24)) {
                                hourPeriod = HourPeriod.Cheia;
                            }

                        } else if (weekDayRange.equals(WeekDayRange.SATURDAY)) {
                            //0:00/2:00 , 6:00/9:00 , 14:00/20:00 , 22:00/24:00
                            if ((hour >= 0 && hour < 2) || (hour >= 6 && hour < 9) || (hour >= 14 && hour < 20) || (hour >= 22 && hour < 24)) {
                                hourPeriod = HourPeriod.Vazio_Normal;
                            }
                            //9:00/14:00 , 20:00/22:00
                            if ((hour >= 9 && hour < 14) || (hour >= 20 && hour < 22)) {
                                hourPeriod = HourPeriod.Cheia;
                            }

                        } else if (weekDayRange.equals(WeekDayRange.SUNDAY)) {
                            //0:00/2:00 , 6:00/24:00
                            if ((hour >= 0 && hour < 2) || (hour >= 6 && hour < 24)) {
                                hourPeriod = HourPeriod.Vazio_Normal;
                            }
                        }
                    }
                }
            }
        }
        consumption.setHourPeriod(hourPeriod);
    }

    private Season extractSeason(Date date) {
        //Spring: March to May
        //Summer: June to August
        //Autumn: September to November
        //Winter: December to February
        /*
            A year is divided into twelve months
            No.	Name	Length in days
            1	January	31
            2	February	28 (29 in leap years)
            3	March	31
            4	April	30
            5	May	31
            6	June	30
            7	July	31
            8	August	31
            9	September	30
            10	October	31
            11	November	30
            12	December	31
        */
        int month = DateUtil.getMonth(date);
        if ((month == 3) || (month == 4) || (month == 5) || (month == 6) || (month == 7) || (month == 8)) {
            return Season.SUMMER;
        } else if ((month == 9) || (month == 10) || (month == 11) || (month == 12) || (month == 1) || (month == 2)) {
            return Season.WINTER;
        }
        return null;

    }

    private WeekDayRange extractWeekDayRange(Date date) {
        int weekDay = DateUtil.getWeekDay(date);
        if ((weekDay == 2) || (weekDay == 3) || (weekDay == 4) || (weekDay == 5) || (weekDay == 6)) {
            return WeekDayRange.MONDAY_TO_FRIDAY;
        } else if (weekDay == 7) {
            return WeekDayRange.SATURDAY;
        } else if (weekDay == 1) {
            return WeekDayRange.SUNDAY;
        }
        return null;
    }


    private float getFloatValueOfHour(String hourStr) {
        float result = 0;
        String tmpHours = hourStr.trim();
        float hours;
        int locationOfColon = tmpHours.indexOf(":");
        hours = Float.parseFloat(tmpHours.substring(0, locationOfColon));
        float minutes = Float.parseFloat(tmpHours.substring(locationOfColon + 1));
        if (minutes > 0) {
            result = minutes / 60;
        }
        result += hours;
        return result;

    }

    public static void main(String[] args) throws ParseException {
        // out.println(getFloatValueOfHourStatic("18:15"));
        // DateUtil.getWeekDay(new Date());
        try {
            FileInputStream file = new FileInputStream(new File("/Users/sahar/IdeaProjects/builtrix-metrics-new/server/Hourly Mat _ FL _ General.xlsx"));

            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowNum = 0;
            for (Row row : sheet) {
                Iterator<Cell> cellIterator = row.cellIterator();
                HistoricalConsumption consumption = null;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    // 7/3/2020  1:00:00 PM
                    out.println(cell.toString());
                    out.println(cell.getCellTypeEnum());
                   /* if (cell.getCellTypeEnum() == CellType.STRING) {//12/3/2020  11:00:00 PM
                        try {
                            Date date = new SimpleDateFormat("dd/MM/yyyy hh:MM:ss a").parse(cell.getStringCellValue());
                            out.println(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }*/
                }
            }
            file.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
