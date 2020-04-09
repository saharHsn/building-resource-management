package tech.builtrix.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

/*
 * Created By Sahar at 4/8/20 : 17:22 PM
 */

@Slf4j
public class ExcelUtil {

    public static XSSFFont getXssfFont(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        // font.setBold(bold);
        return font;
    }

    public static CellStyle getCellStyle(XSSFWorkbook workbook, XSSFFont font) {
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        // style.setAlignment(HorizontalAlignment.RIGHT);
        style.setBorderBottom(BorderStyle.HAIR);
        style.setBorderTop(BorderStyle.HAIR);
        style.setBorderLeft(BorderStyle.HAIR);
        style.setBorderRight(BorderStyle.HAIR);
        style.setBottomBorderColor((short) 0x8);
        style.setFont(font);
        return style;
    }

    public static void createMergedRow(XSSFSheet sheet,
                                       XSSFFont font,
                                       String title,
                                       int firstRow,
                                       int lastRow,
                                       int firstCol,
                                       int lastCol,
                                       Row row) {
        Cell cell = row.createCell(firstCol);
        cell.setCellValue(title);
        font.setBold(true);
        CellStyle cellStyle = cell.getCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setFont(font);
        cellStyle.setFillBackgroundColor((short) 0x8);
        cell.setCellStyle(cellStyle);
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

    public static void createHeaderRow(XSSFSheet sheet, List<String> headers) {
        Row row = sheet.createRow(0);
        for (String header : headers) {
            Cell cell1 = row.createCell(headers.indexOf(header));
            cell1.setCellValue(header);
        }
    }
}
