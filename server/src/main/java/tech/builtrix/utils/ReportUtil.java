package tech.builtrix.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import tech.builtrix.models.building.enums.EnergyCertificate;
import tech.builtrix.services.report.BillParamInfo;
import tech.builtrix.services.report.DataType;
import tech.builtrix.services.report.ReportData;
import tech.builtrix.services.report.WeekDayInfo;
import tech.builtrix.web.dtos.bill.BillDto;
import tech.builtrix.web.dtos.bill.BuildingDto;
import tech.builtrix.web.dtos.historical.HistoricalEnergyConsumptionDto;
import tech.builtrix.web.dtos.report.ConsumptionDto;
import tech.builtrix.web.dtos.report.ConsumptionDynamicDto;
import tech.builtrix.web.dtos.report.CostStackDto;
import tech.builtrix.web.dtos.report.HistoricalConsumptionDto;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created By sahar at 12/11/19
 */
public class ReportUtil {
    public static Float getRankOfBuilding(EnergyCertificate energyCertificate) {
        // TODO read values from National DB
        CertificateInfo aPlus = new CertificateInfo(1, 1.8f, EnergyCertificate.APlus);
        CertificateInfo a = new CertificateInfo(2, 8.3f, EnergyCertificate.A);
        CertificateInfo b = new CertificateInfo(3, 9.7f, EnergyCertificate.B);
        CertificateInfo bMinus = new CertificateInfo(4, 9.7f, EnergyCertificate.BMinus);
        CertificateInfo c = new CertificateInfo(5, 26.4f, EnergyCertificate.C);
        CertificateInfo d = new CertificateInfo(6, 22.8f, EnergyCertificate.D);
        CertificateInfo e = new CertificateInfo(7, 13.5f, EnergyCertificate.E);
        CertificateInfo f = new CertificateInfo(8, 7.8f, EnergyCertificate.F);
        List<CertificateInfo> certificateInfoList = Arrays.asList(aPlus, a, b, bMinus, c, d, e, f);
        CertificateInfo certificateInfo = findCertificateInfo(energyCertificate, certificateInfoList);
        Float sum = 0f;
        for (CertificateInfo info : certificateInfoList) {
            if (info.getRank() <= certificateInfo.getRank()) {
                sum += info.getValue();
            }
        }
        return sum;
    }

    private static CertificateInfo findCertificateInfo(EnergyCertificate energyCertificate,
                                                       List<CertificateInfo> certificateInfoList) {
        for (CertificateInfo certificateInfo : certificateInfoList) {
            if (certificateInfo.getCertificateType().equals(energyCertificate))
                return certificateInfo;
        }
        return null;
    }

    public static String getDateTitle(Integer month, int year) {
        return DateUtil.getMonth(month) + "-" + year;
    }

    public static List<Float> getSavings(Float month1Cost, Float month2Cost, Float month3Cost, int billsSize) {
        Float savings1 = ReportUtil.roundDecimal(0.05f * (month1Cost));
        Float savings2 = ReportUtil.roundDecimal(0.05f * (month2Cost));
        Float savings3 = ReportUtil.roundDecimal(0.05f * (month3Cost));
        return Arrays.asList(savings1, savings2, savings3);
    }

    public static float roundDecimal(float input) {
        DecimalFormat df = new DecimalFormat("0.00");
        return Float.parseFloat(df.format(input));
    }

    public static void main(String[] args) {
        System.out.println(roundDecimal(324.449494949f));
    }

    public static Date getCurrentDate() {
        return new Date();
    }

    public static CostStackDto getCostStackDto(List<BillDto> dtoList) {
        CostStackDto dto = new CostStackDto();
        List<Float> contractedPowerValues = new ArrayList<>();
        List<Float> powerInPeakValues = new ArrayList<>();
        List<Float> reactivePowerValues = new ArrayList<>();
        List<Float> normalValues = new ArrayList<>();
        List<Float> peakValues = new ArrayList<>();
        List<Float> freeValues = new ArrayList<>();
        List<Float> offValues = new ArrayList<>();
        extractCostValues(dtoList, contractedPowerValues, powerInPeakValues, reactivePowerValues, normalValues,
                peakValues, freeValues, offValues);
        dto.setContractedPowerValues(contractedPowerValues);
        dto.setFreeValues(freeValues);
        dto.setOffValues(offValues);
        dto.setPeakValues(peakValues);
        dto.setPowerInPeakValues(powerInPeakValues);
        dto.setNormalValues(normalValues);
        dto.setReactivePowerValues(reactivePowerValues);
        // TODO refine later
        dto.setXValues(
                Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"));
        return dto;
    }

    private static void extractCostValues(List<BillDto> dtoList, List<Float> contractedPowerValues,
                                          List<Float> powerInPeakValues, List<Float> reactivePowerValues, List<Float> normalValues,
                                          List<Float> peakValues, List<Float> freeValues, List<Float> offValues) {
        for (BillDto billDto : dtoList) {
            if (billDto.getRDContractedPower() != null) {
                contractedPowerValues.add(ReportUtil.roundDecimal(billDto.getRDContractedPower().getTotalTariffCost()));
            }
            float aeFree = 0f;
            float rdFree = 0f;
            if (billDto.getAEFreeHours() != null) {
                aeFree = billDto.getAEFreeHours().getTotalTariffCost();
            }
            if (billDto.getRDFreeHours() != null) {
                rdFree = billDto.getRDFreeHours().getTotalTariffCost();
            }
            freeValues.add(ReportUtil.roundDecimal(aeFree + rdFree));

            float aeOff = 0f;
            float rdOff = 0f;
            if (billDto.getAEOffHours() != null) {
                aeOff = billDto.getAEOffHours().getTotalTariffCost();
            }
            if (billDto.getRDOffHours() != null) {
                rdOff = billDto.getRDOffHours().getTotalTariffCost();
            }
            offValues.add(ReportUtil.roundDecimal(aeOff + rdOff));

            float aePeak = 0f;
            float rdPeak = 0f;
            if (billDto.getAEPeakHours() != null) {
                aePeak = billDto.getAEPeakHours().getTotalTariffCost();
            }
            if (billDto.getRDPeakHours() != null) {
                rdPeak = billDto.getRDPeakHours().getTotalTariffCost();
            }
            peakValues.add(ReportUtil.roundDecimal(aePeak + rdPeak));


            if (billDto.getRDPeakHours() != null) {
                powerInPeakValues.add(ReportUtil.roundDecimal(billDto.getRDPeakHours().getTotalTariffCost()));
            }

            float aeNormal = 0f;
            float rdNormal = 0f;
            if (billDto.getAENormalHours() != null) {
                aeNormal = billDto.getAENormalHours().getTotalTariffCost();
            }
            if (billDto.getRDNormalHours() != null) {
                rdNormal = billDto.getRDNormalHours().getTotalTariffCost();
            }
            normalValues.add(ReportUtil.roundDecimal(aeNormal + rdNormal));


            if (billDto.getRDReactivePower() != null) {
                reactivePowerValues.add(ReportUtil.roundDecimal(billDto.getRDReactivePower().getTotalTariffCost()));
            }
        }
    }

    private static void extractHistoricalConsumptionValues(List<HistoricalEnergyConsumptionDto> dtoList,
                                                           DataType dataType, List<Float> contractedPowerValues,
                                                           List<Float> powerInPeakValues,
                                                           List<Float> reactivePowerValues,
                                                           List<Float> normalValues,
                                                           List<Float> peakValues,
                                                           List<Float> freeValues,
                                                           List<Float> offValues) {
        for (HistoricalEnergyConsumptionDto consumptionDto : dtoList) {
            float consumption = consumptionDto.getConsumption();
            float cost = roundDecimal(consumptionDto.getCost());
            switch (consumptionDto.getHourPeriod()) {
                case Vazio_Normal:
                    switch (dataType) {
                        case CONSUMPTION:
                            freeValues.add(consumption);
                            break;
                        case COST:
                            freeValues.add(cost);
                            break;
                    }
                    break;
                case Super_Vazio:
                    switch (dataType) {
                        case CONSUMPTION:
                            offValues.add(consumption);
                            break;
                        case COST:
                            offValues.add(cost);
                            break;
                    }
                    break;
                case Cheia:
                    switch (dataType) {
                        case CONSUMPTION:
                            normalValues.add(consumption);
                            break;
                        case COST:
                            normalValues.add(cost);
                            break;
                    }
                    break;
                case Ponta:
                    switch (dataType) {
                        case CONSUMPTION:
                            peakValues.add(consumption);
                            break;
                        case COST:
                            peakValues.add(cost);
                            break;
                    }
                    break;
            }
        }

    }

    public static ConsumptionDto getConsumptionDto(List<BillDto> dtoList, Integer year, boolean isConsumption) {
        ConsumptionDto dto = new ConsumptionDto();
        // TODO refine later
        dto.setXValues(
                Arrays.asList("Jan-" + year, "Feb-" + year, "Mar-" + year, "Apr-" + year, "May-" + year, "Jun-" + year,
                        "Jul-" + year, "Aug-" + year, "Sept-" + year, "Oct-" + year, "Nov-" + year, "Dec-" + year));

        List<Float> contractedPowerValues = new ArrayList<>(Collections.nCopies(12, 0f));
        List<Float> powerInPeakValues = new ArrayList<>(Collections.nCopies(12, 0f));
        List<Float> reactivePowerValues = new ArrayList<>(Collections.nCopies(12, 0f));
        List<Float> normalValues = new ArrayList<>(Collections.nCopies(12, 0f));
        List<Float> peakValues = new ArrayList<>(Collections.nCopies(12, 0f));
        List<Float> freeValues = new ArrayList<>(Collections.nCopies(12, 0f));
        List<Float> offValues = new ArrayList<>(Collections.nCopies(12, 0f));
        extractConsumptionValues(dtoList, contractedPowerValues, powerInPeakValues, reactivePowerValues, normalValues,
                peakValues, freeValues, offValues, isConsumption);
        dto.setContractedPowerValues(contractedPowerValues);
        dto.setFreeValues(freeValues);
        dto.setOffValues(offValues);
        dto.setPeakValues(peakValues);
        dto.setPowerInPeakValues(powerInPeakValues);
        dto.setNormalValues(normalValues);
        dto.setReactivePowerValues(reactivePowerValues);
        return dto;
    }

    private static void extractConsumptionValues(List<BillDto> dtoList, List<Float> contractedPowerValues,
                                                 List<Float> powerInPeakValues, List<Float> reactivePowerValues, List<Float> normalValues,
                                                 List<Float> peakValues, List<Float> freeValues, List<Float> offValues, boolean isConsumption) {
        for (BillDto billDto : dtoList) {
            if (!billDtoIsNull(billDto)) {
                int index = billDto.getFromMonth() - 1;
                if (billDto.getRDContractedPower() != null) {
                    float consumption = isConsumption ? billDto.getRDContractedPower().getConsumption()
                            : billDto.getRDContractedPower().getTotalTariffCost();
                    float e = ReportUtil.roundDecimal(consumption);
                    contractedPowerValues.set(index, e);
                }
                if (billDto.getAEFreeHours() != null) {
                    float consumption = isConsumption ? billDto.getAEFreeHours().getConsumption()
                            : billDto.getAEFreeHours().getTotalTariffCost();
                    float e = ReportUtil.roundDecimal(consumption);
                    freeValues.set(index, e);
                }
                if (billDto.getAEOffHours() != null) {
                    float consumption = isConsumption ? billDto.getAEOffHours().getConsumption()
                            : billDto.getAEOffHours().getTotalTariffCost();
                    float e = ReportUtil.roundDecimal(consumption);
                    offValues.set(index, e);
                }
                if (billDto.getAEPeakHours() != null) {
                    float consumption = isConsumption ? billDto.getAEPeakHours().getConsumption()
                            : billDto.getAEPeakHours().getTotalTariffCost();
                    float e = ReportUtil.roundDecimal(consumption);
                    peakValues.set(index, e);
                }
                if (billDto.getRDPeakHours() != null) {
                    float consumption = isConsumption ? billDto.getRDPeakHours().getConsumption()
                            : billDto.getRDPeakHours().getTotalTariffCost();
                    float e = ReportUtil.roundDecimal(consumption);
                    powerInPeakValues.set(index, e);
                }
                if (billDto.getAENormalHours() != null) {
                    float consumption = isConsumption ? billDto.getAENormalHours().getConsumption()
                            : billDto.getAENormalHours().getTotalTariffCost();
                    float e = ReportUtil.roundDecimal(consumption);
                    normalValues.set(index, e);
                }
                if (billDto.getRDReactivePower() != null) {
                    float consumption = isConsumption ? billDto.getRDReactivePower().getConsumption()
                            : billDto.getRDReactivePower().getTotalTariffCost();
                    float e = ReportUtil.roundDecimal(consumption);
                    reactivePowerValues.set(index, e);
                }
            }
        }
    }

    private static boolean billDtoIsNull(BillDto billDto) {
        return (billDto.getBuildingId() == null && billDto.getFromDate() == null && billDto.getToDate() == null);
    }

    // TODO reimplement later
    public static ConsumptionDynamicDto getConsumptionDynamicDto(String s, String s2, float v, float v2, float v3,
                                                                 float v4) {
        ConsumptionDynamicDto freeHoursQ = new ConsumptionDynamicDto();
        freeHoursQ.setColor(s);
        freeHoursQ.setName(s2);
        freeHoursQ.setData(Arrays.asList(v, v2, v3, v4));
        return freeHoursQ;
    }

    public static ConsumptionDynamicDto getConsumptionDynamicDto(String color, String name, List<Float> data) {
        ConsumptionDynamicDto dto = new ConsumptionDynamicDto();
        dto.setColor(color);
        dto.setName(name);
        dto.setData(data);
        return dto;
    }

    public static EnergyCertificate increaseEnergyCertificate(EnergyCertificate energyCertificate) {
        if (energyCertificate.equals(EnergyCertificate.Others)) {
            return EnergyCertificate.F;
        } else if (energyCertificate.equals(EnergyCertificate.F)) {
            return EnergyCertificate.E;
        } else if (energyCertificate.equals(EnergyCertificate.E)) {
            return EnergyCertificate.D;
        } else if (energyCertificate.equals(EnergyCertificate.D)) {
            return EnergyCertificate.C;
        } else if (energyCertificate.equals(EnergyCertificate.C)) {
            return EnergyCertificate.BMinus;
        } else if (energyCertificate.equals(EnergyCertificate.BMinus)) {
            return EnergyCertificate.B;
        } else if (energyCertificate.equals(EnergyCertificate.B)) {
            return EnergyCertificate.A;
        }
        if (energyCertificate.equals(EnergyCertificate.A)) {
            return EnergyCertificate.APlus;
        }
        return EnergyCertificate.Others;
    }

    public static EnergyCertificate getEnergyEfficiency(Float efficiencyLevel, List<BillDto> billsOfYear) {
        EnergyCertificate energyEfficiency = EnergyCertificate.Others;
        if (efficiencyLevel >= 0 && efficiencyLevel < 25) {
            energyEfficiency = EnergyCertificate.APlus;
        }
        if (efficiencyLevel >= 26 && efficiencyLevel < 50) {
            energyEfficiency = EnergyCertificate.A;
        }
        if (efficiencyLevel >= 51 && efficiencyLevel < 75) {
            energyEfficiency = EnergyCertificate.B;
        }
        if (efficiencyLevel >= 76 && efficiencyLevel < 100) {
            energyEfficiency = EnergyCertificate.BMinus;
        }
        if (efficiencyLevel >= 101 && efficiencyLevel < 150) {
            energyEfficiency = EnergyCertificate.C;
        }
        if (efficiencyLevel >= 151 && efficiencyLevel < 200) {
            energyEfficiency = EnergyCertificate.D;
        }
        if (efficiencyLevel >= 201 && efficiencyLevel < 250) {
            energyEfficiency = EnergyCertificate.E;
        }
        if (efficiencyLevel >= 250) {
            energyEfficiency = EnergyCertificate.F;
        }
        return energyEfficiency;
    }

    public static void updateList(List<Float> standardAVals, List<Float> standardBVals, List<Float> standardCVals,
                                  List<Float> standardDVals, Float efficiencyLevel, EnergyCertificate efficiency, int month) {
        if (efficiency.equals(EnergyCertificate.A) || efficiency.equals(EnergyCertificate.APlus)) {
            standardAVals.add(month, efficiencyLevel);
        } else if (efficiency.equals(EnergyCertificate.B) || efficiency.equals(EnergyCertificate.BMinus)) {
            standardBVals.add(month, efficiencyLevel);
        } else if (efficiency.equals(EnergyCertificate.C)) {
            standardCVals.add(month, efficiencyLevel);
        } else if (efficiency.equals(EnergyCertificate.D) || efficiency.equals(EnergyCertificate.F)
                || efficiency.equals(EnergyCertificate.E)) {
            standardDVals.add(month, efficiencyLevel);
        }
    }

    public static HistoricalConsumptionDto getHistoricalConsumption(List<HistoricalEnergyConsumptionDto> dtoList,
                                                                    DataType dataType) {
        HistoricalConsumptionDto dto = new HistoricalConsumptionDto();
        List<Float> contractedPowerValues = new ArrayList<>();
        List<Float> powerInPeakValues = new ArrayList<>();
        List<Float> reactivePowerValues = new ArrayList<>();
        List<Float> normalValues = new ArrayList<>();
        List<Float> peakValues = new ArrayList<>();
        List<Float> freeValues = new ArrayList<>();
        List<Float> offValues = new ArrayList<>();
        extractHistoricalConsumptionValues(dtoList,
                dataType,
                contractedPowerValues,
                powerInPeakValues,
                reactivePowerValues,
                normalValues,
                peakValues,
                freeValues,
                offValues);
        dto.setFreeValues(freeValues);
        dto.setOffValues(offValues);
        dto.setPeakValues(peakValues);
        dto.setNormalValues(normalValues);
        // TODO refine later
        List<String> xValues = new ArrayList<>();
        for (HistoricalEnergyConsumptionDto consumptionDto : dtoList) {
            Integer dayOfMonth = DateUtil.getDayOfMonth(consumptionDto.getDate());
            if (!xValues.contains(dayOfMonth.toString())) {
                xValues.add(String.valueOf(dayOfMonth));
            }
        }
        dto.setXValues(xValues);
        return dto;
    }


    //--------------------------------- Excel methods ---------------------------------------

    public static void createMetaDataSheet(BuildingDto buildingDto, XSSFWorkbook workbook, String sheetTitle, CellStyle cellStyle, XSSFFont font) {
        XSSFSheet metaDataSheet = workbook.createSheet(sheetTitle);
        fillMetaDataSheet(buildingDto, metaDataSheet, cellStyle);
    }

    public static void createDataSheet(XSSFWorkbook workbook,
                                       CellStyle style,
                                       XSSFFont font,
                                       List<BillDto> billsOfLastYear,
                                       List<BillDto> billsOfCurrentYear,
                                       List<BillDto> billsOfLast12Month,
                                       DataType dataType,
                                       String sheetTitle) {
        XSSFSheet sheet = workbook.createSheet(sheetTitle);
        Row row = sheet.createRow(0);
        ExcelUtil.createMergedRow(sheet, font, "2019", 0, 0, 0, 12, row);
        ExcelUtil.createMergedRow(sheet, font, "2020", 0, 0, 13, 24, row);
        ExcelUtil.createMergedRow(sheet, font, "Average Monthly", 0, 0, 25, 26, row);
        ExcelUtil.createMergedRow(sheet, font, "Total Annual", 0, 0, 27, 28, row);

        List<String> secondHeader = Arrays.asList("Trf",
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
                "AVE-19", "AVE-20", "2019", "Last 12 Month");
        writeHeader(secondHeader, sheet, font, style);
        ReportData data = fillReportData(billsOfLastYear, billsOfCurrentYear, billsOfLast12Month, dataType);
        makeParamTable(sheet, data, 2, style, font);

    }

    private static void writeHeader(List<String> secondHeader, XSSFSheet sheet, XSSFFont font, CellStyle style) {
        XSSFRow row = sheet.createRow(1);
        int columnNumber = 0;
        for (String header : secondHeader) {
            XSSFCell cell = row.createCell(columnNumber);
            style.setAlignment(HorizontalAlignment.CENTER);
            font.setBold(true);
            style.setFont(font);
            cell.setCellValue(header);
            sheet.autoSizeColumn(columnNumber);
            columnNumber++;
        }
    }

    public static void createReferenceSheet(XSSFWorkbook workbook, XSSFFont font, String sheetTitle) {
        XSSFSheet sheet = workbook.createSheet(sheetTitle);
        List<String> headers = Arrays.asList("Tariffs", "Winter", "Summer", "2019", "2020", "Currency", "Iva");
        ExcelUtil.createHeaderRow(sheet, headers);
        Row row1 = sheet.createRow(1);
        ExcelUtil.createMergedRow(sheet, font, "Monday-Friday", 1, 1, 0, 6, row1);
        createWeekDayTable(sheet, 3);
        Row row2 = sheet.createRow(6);
        ExcelUtil.createMergedRow(sheet, font, "Saturday", 6, 6, 0, 6, row2);
        createWeekDayTable(sheet, 7);
        Row row3 = sheet.createRow(11);
        ExcelUtil.createMergedRow(sheet, font, "Saturday", 11, 11, 0, 6, row3);
        createWeekDayTable(sheet, 12);
    }

    //TODO optimize this method
    public static ReportData fillReportData(List<BillDto> billsOfLastYear,
                                            List<BillDto> billsOfCurrentYear,
                                            List<BillDto> billsOfLast12Month,
                                            DataType dataType) {
        ReportData reportData = new ReportData();
        List<Object> svList = new ArrayList<>();
        List<Object> vnList = new ArrayList<>();
        List<Object> pList = new ArrayList<>();
        List<Object> cList = new ArrayList<>();
        List<Object> totList = new ArrayList<>();
        svList.add("SV");
        vnList.add("VN");
        pList.add("P");
        cList.add("C");
        totList.add("Tot");

        float lastYearSvTot = 0f;
        float lastYearVnTot = 0f;
        float lastYearPTot = 0f;
        float lastYearCTot = 0f;
        for (BillDto billDto : billsOfLastYear) {
            float svCons;
            if (dataType.equals(DataType.CONSUMPTION)) {
                svCons = billDto.getAEOffHours().getConsumption();
            } else {
                svCons = billDto.getAEOffHours().getTotalTariffCost();
            }
            lastYearSvTot += svCons;
            svList.add(ReportUtil.roundDecimal(svCons));

            float vnCons;
            if (dataType.equals(DataType.CONSUMPTION)) {
                vnCons = billDto.getAEFreeHours().getConsumption();
            } else {
                vnCons = billDto.getAEFreeHours().getTotalTariffCost();
            }
            lastYearVnTot += vnCons;
            vnList.add(ReportUtil.roundDecimal(vnCons));

            float pCons;
            if (dataType.equals(DataType.CONSUMPTION)) {
                pCons = billDto.getAEPeakHours().getConsumption();
            } else {
                pCons = billDto.getAEPeakHours().getTotalTariffCost();
            }
            lastYearPTot += pCons;
            pList.add(ReportUtil.roundDecimal(pCons));

            float cCons;
            if (dataType.equals(DataType.CONSUMPTION)) {
                cCons = billDto.getAENormalHours().getConsumption();
            } else {
                cCons = billDto.getAENormalHours().getTotalTariffCost();
            }
            lastYearCTot += cCons;
            cList.add(ReportUtil.roundDecimal(cCons));
        }

        float currentSvTot = 0f;
        float currentVnTot = 0f;
        float currentPTot = 0f;
        float currentCTot = 0f;
        for (BillDto billDto : billsOfCurrentYear) {
            float svValue = 0f;
            if (dataType.equals(DataType.CONSUMPTION)) {
                if (billDto.getAEOffHours() != null) {
                    svValue = billDto.getAEOffHours().getConsumption();
                }
            } else {
                if (billDto.getAEOffHours() != null) {
                    svValue = billDto.getAEOffHours().getTotalTariffCost();
                }
            }
            currentSvTot += svValue;
            svList.add(ReportUtil.roundDecimal(svValue));

            float vnValue = 0f;
            if (billDto.getAEFreeHours() != null) {
                if (dataType.equals(DataType.CONSUMPTION)) {
                    vnValue = billDto.getAEFreeHours().getConsumption();
                } else {
                    vnValue = billDto.getAEFreeHours().getTotalTariffCost();
                }
            }
            currentVnTot += vnValue;
            vnList.add(ReportUtil.roundDecimal(vnValue));

            float pValue = 0f;
            if (billDto.getAEPeakHours() != null) {
                if (dataType.equals(DataType.CONSUMPTION)) {
                    pValue = billDto.getAEPeakHours().getConsumption();
                } else {
                    pValue = billDto.getAEPeakHours().getTotalTariffCost();
                }
            }

            currentPTot += pValue;
            pList.add(ReportUtil.roundDecimal(pValue));

            float cValue = 0f;
            if (billDto.getAENormalHours() != null) {
                if (dataType.equals(DataType.CONSUMPTION)) {
                    cValue = billDto.getAENormalHours().getConsumption();
                } else {
                    cValue = billDto.getAENormalHours().getTotalTariffCost();
                }
            }
            currentCTot += cValue;
            cList.add(ReportUtil.roundDecimal(cValue));
        }

        float last12SvTot = 0f;
        float last12VnTot = 0f;
        float last12PTot = 0f;
        float last12CTot = 0f;
        for (BillDto billDto : billsOfLast12Month) {
            float svValue = 0f;
            if (billDto.getAEOffHours() != null) {
                if (dataType.equals(DataType.CONSUMPTION)) {
                    svValue = billDto.getAEOffHours().getConsumption();
                } else {
                    svValue = billDto.getAEOffHours().getTotalTariffCost();
                }
            }
            last12SvTot += svValue;
            // svList.add(String.valueOf(svValue));

            float vnValue = 0f;
            if (billDto.getAEFreeHours() != null) {
                if (dataType.equals(DataType.CONSUMPTION)) {
                    vnValue = billDto.getAEFreeHours().getConsumption();
                } else {
                    vnValue = billDto.getAEFreeHours().getTotalTariffCost();
                }
            }
            last12VnTot += vnValue;
            // vnList.add(String.valueOf(vnValue));

            float pValue = 0f;
            if (billDto.getAEPeakHours() != null) {
                if (dataType.equals(DataType.CONSUMPTION)) {
                    pValue = billDto.getAEPeakHours().getConsumption();
                } else {
                    pValue = billDto.getAEPeakHours().getTotalTariffCost();
                }
            }
            last12PTot += pValue;
            // pList.add(String.valueOf(pValue));

            float cValue = 0f;
            if (billDto.getAENormalHours() != null) {
                if (dataType.equals(DataType.CONSUMPTION)) {
                    cValue = billDto.getAENormalHours().getConsumption();
                } else {
                    cValue = billDto.getAENormalHours().getTotalTariffCost();
                }
            }
            last12CTot += cValue;
            // cList.add(String.valueOf(cValue));
        }

        svList.add(ReportUtil.roundDecimal((lastYearSvTot / 12)));
        svList.add(ReportUtil.roundDecimal((currentSvTot / 12)));
        svList.add((ReportUtil.roundDecimal(lastYearSvTot)));
        svList.add(ReportUtil.roundDecimal(last12SvTot));

        vnList.add(ReportUtil.roundDecimal((lastYearVnTot / 12)));
        vnList.add(ReportUtil.roundDecimal((currentVnTot / 12)));
        vnList.add((ReportUtil.roundDecimal(lastYearVnTot)));
        vnList.add((ReportUtil.roundDecimal(last12VnTot)));

        pList.add(ReportUtil.roundDecimal((lastYearPTot / 12)));
        pList.add(ReportUtil.roundDecimal((currentPTot / 12)));
        pList.add((ReportUtil.roundDecimal(lastYearPTot)));
        pList.add((ReportUtil.roundDecimal(last12PTot)));

        cList.add(ReportUtil.roundDecimal((lastYearCTot / 12)));
        cList.add(ReportUtil.roundDecimal((currentCTot / 12)));
        cList.add((ReportUtil.roundDecimal(lastYearCTot)));
        cList.add((ReportUtil.roundDecimal(last12CTot)));
        for (int i = 1; i <= 28; i++) {
            float add = ((Float) svList.get(i)) + ((Float) vnList.get(i)) + ((Float) pList.get(i)) + ((Float) cList.get(i));
            totList.add(ReportUtil.roundDecimal(add));
        }
        reportData.setSVList(svList);
        reportData.setVNList(vnList);
        reportData.setPList(pList);
        reportData.setCList(cList);
        reportData.setTotList(totList);
        return reportData;
    }

    public static void makeParamTable(XSSFSheet sheet, ReportData data, int rowNum, CellStyle style, XSSFFont font) {
        int cellNum = 0;
        // int rowNum = 1;

        createParamCell(sheet, cellNum, rowNum, data.getSVList(), style, font);
        rowNum++;
        cellNum = 0;
        createParamCell(sheet, cellNum, rowNum, data.getVNList(), style, font);
        rowNum++;
        cellNum = 0;
        createParamCell(sheet, cellNum, rowNum, data.getPList(), style, font);
        rowNum++;
        cellNum = 0;
        createParamCell(sheet, cellNum, rowNum, data.getCList(), style, font);
        rowNum++;
        cellNum = 0;
        createParamCell(sheet, cellNum, rowNum, data.getTotList(), style, font);
    }

    public static void createParamCell(XSSFSheet sheet, int cellNum, int rowNum, List<Object> dataList, CellStyle style, XSSFFont font) {
        Row row = sheet.createRow(rowNum);
        for (Object sv : dataList) {
            sheet.autoSizeColumn(cellNum);
            Cell cell = row.createCell(cellNum);
            String strCellValue;
            float floatCellValue;
            if (sv instanceof String) {
                strCellValue = String.valueOf(sv);
                cell.setCellValue(strCellValue);
            } else if (sv instanceof Float) {
                floatCellValue = (Float) sv;
                cell.setCellValue(floatCellValue);
            }
            font.setBold(false);
            style.setFont(font);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(cellNum);
            cellNum++;
        }
    }

    public static void createWeekDayTable(XSSFSheet sheet, int rowNumber) {
        BillParamInfo superVazio = new BillParamInfo("Super Vazio (SV)", "2:00/6:00", "2:00/6:00", "0.070750", "0.070750", "€", "23%");
        BillParamInfo vazioNormal = new BillParamInfo("Vazio Normal (VN)", "0:00/2:00", "6:00/7:00", "0:00/2:00", "6:00/7:00", "€", "23%");
        BillParamInfo ponta = new BillParamInfo("Ponta (P)", "9:30/12:00 , 18:30/21", "9:15/12:15", "0.089990", "0.089990", "€", "23%");
        BillParamInfo cheia = new BillParamInfo("Cheia (C)", "7:00/9:30 , 12:00/18:30 , 21:00/24:00", "7:00/9:15 , 12:15/24:00", "7:00/9:15 , 12:15/24:00\t0.089700", "0.089700", "€", "23%");
        WeekDayInfo weekDayInfo = new WeekDayInfo();
        weekDayInfo.setSuperVazio(superVazio);
        weekDayInfo.setVazioNormal(vazioNormal);
        weekDayInfo.setPonta(ponta);
        weekDayInfo.setCheia(cheia);
        Row superVazioRow = sheet.createRow(rowNumber);
        creatBillParamTable(weekDayInfo.getSuperVazio(), superVazioRow, sheet);
        Row vazioNormalRow = sheet.createRow(++rowNumber);
        creatBillParamTable(weekDayInfo.getVazioNormal(), vazioNormalRow, sheet);
        Row pontaRow = sheet.createRow(++rowNumber);
        creatBillParamTable(weekDayInfo.getPonta(), pontaRow, sheet);
        Row cheiaRow = sheet.createRow(++rowNumber);
        creatBillParamTable(weekDayInfo.getCheia(), cheiaRow, sheet);
    }

    public static void creatBillParamTable(BillParamInfo paramInfo, Row row, XSSFSheet sheet) {
        int cellNum = 0;
        Cell cell0 = row.createCell(cellNum);
        cell0.setCellValue(paramInfo.getName());
        sheet.autoSizeColumn(cellNum);

        Cell cell1 = row.createCell(++cellNum);
        cell1.setCellValue(paramInfo.getFromToHour1());
        sheet.autoSizeColumn(cellNum);

        Cell cell2 = row.createCell(++cellNum);
        cell2.setCellValue(paramInfo.getFromToHour2());
        sheet.autoSizeColumn(cellNum);

        Cell cell3 = row.createCell(++cellNum);
        cell3.setCellValue(paramInfo.getCost1());
        sheet.autoSizeColumn(cellNum);

        Cell cell4 = row.createCell(++cellNum);
        cell4.setCellValue(paramInfo.getCost2());
        sheet.autoSizeColumn(cellNum);

        Cell cell5 = row.createCell(++cellNum);
        cell5.setCellValue(paramInfo.getCurrency());
        sheet.autoSizeColumn(cellNum);

        Cell cell6 = row.createCell(++cellNum);
        cell6.setCellValue(paramInfo.getPercentage());
        sheet.autoSizeColumn(cellNum);
    }

    public static void fillMetaDataSheet(BuildingDto metaData, XSSFSheet metaDataSheet, CellStyle style) {
        createRowAndCell(metaDataSheet, 0, "Name of the Building", metaData.getName(), style);
        createRowAndCell(metaDataSheet, 1, "Type of the Building", metaData.getUsage(), style);
        createRowAndCell(metaDataSheet, 2, "Area", metaData.getArea(), style);
        createRowAndCell(metaDataSheet, 3, "Average Number of People", metaData.getNumberOfPeople(), style);
        createRowAndCell(metaDataSheet, 4, "Location", metaData.getPostalAddress(), style);
        createRowAndCell(metaDataSheet, 5, "Contracted Power", 0f, style);
    }

    public static void createRowAndCell(XSSFSheet metaDataSheet, int rowNumber, String subject, Object value, CellStyle style) {
        Row row = metaDataSheet.createRow(rowNumber);
        Cell cell1 = row.createCell(0);
        cell1.setCellValue(subject);
        cell1.setCellStyle(style);
        metaDataSheet.autoSizeColumn(0);
        Cell cell2 = row.createCell(1);
        cell2.setCellValue(String.valueOf(value));
        metaDataSheet.autoSizeColumn(1);
        cell2.setCellStyle(style);
    }


    //--------------------------------- Excel methods ---------------------------------------

/*
    public static EnergyCertificate getNationalMedianCert() {
        // TODO read this values from national database
        float eeAPlus = 7021f;
        float eeA = 15839f;
        float eeB = 20887f;
        float eeBMinus = 49069f;
        float eeC = 229994f;
        float eeD = 249806f;
        float eeE = 152569f;
        float eeF = 83306f;
        return getEnergyEfficiency((eeAPlus + eeA + eeB + eeBMinus + eeC + eeD + eeE + eeF) / 8, billsOfYear);
    }*/

   /* public static EnergyCertificate getPropertyTargetCert(EnergyCertificate baseLineEE) {
        switch (baseLineEE) {
            case A:
                return EnergyCertificate.APlus;
            case B:
                return EnergyCertificate.A;
            case BMinus:
                return EnergyCertificate.B;
            case C:
                return EnergyCertificate.BMinus;
            case D:
                return EnergyCertificate.C;
            case E:
                return EnergyCertificate.D;
            case F:
                return EnergyCertificate.E;
        }
        return EnergyCertificate.Others;
    }*/

    @AllArgsConstructor
    @Getter
    private static class CertificateInfo {
        int rank;
        Float value;
        EnergyCertificate certificateType;
    }
}
