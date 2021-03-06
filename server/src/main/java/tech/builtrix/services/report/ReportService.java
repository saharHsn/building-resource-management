package tech.builtrix.services.report;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.models.bill.Bill;
import tech.builtrix.models.building.Building;
import tech.builtrix.models.building.enums.EnergyCertificate;
import tech.builtrix.services.bill.BillService;
import tech.builtrix.services.building.BuildingService;
import tech.builtrix.utils.DateUtil;
import tech.builtrix.utils.ExcelUtil;
import tech.builtrix.utils.ReportUtil;
import tech.builtrix.utils.WeatherUtil;
import tech.builtrix.web.dtos.bill.BillDto;
import tech.builtrix.web.dtos.bill.BillParameterDto;
import tech.builtrix.web.dtos.bill.BuildingDto;
import tech.builtrix.web.dtos.bill.ReportIndex;
import tech.builtrix.web.dtos.report.*;
import tech.builtrix.web.dtos.report.enums.DatePartType;
import tech.builtrix.web.dtos.report.enums.TimePeriodType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created By sahar at 12/4/19
 */
@Component
@Slf4j
public class ReportService {
    // private static float kg_CO2_per_each_kWh = 0.408f;
    private final static float COMFORT_TEMPERATURE = 20f;
    private final static float ANNUAL_EFFICIENCY_REF = 51f;
    private static float PROPERTY_TARGET_COEFFICIENT = 0.95f;
    private static float CO2_CONS = 0.3332f; // kg/Kwh
    private final static Float REFERENCE = 50f;
    private final static float A_PLUS_CONSUMPTION = 12.5f; //25*REFERENCE
    private final static float F_CONSUMPTION = 150f;  //300*REFERENCE
    private final static float PROPERTY_TARGET_CONST = 1.05f;  //300*REFERENCE

    private final BillService billService;
    private final BuildingService buildingService;

    @Autowired
    public ReportService(BillService billService, BuildingService buildingService) {
        this.billService = billService;
        this.buildingService = buildingService;
    }

    public PredictionDto predict(String buildingId) throws NotFoundException {
        // 1. find next three months
        // 2. find next three months data for previous years
        // 3. average n previous year data
        // 4. return each month data
        BuildingDto building = this.buildingService.findById(buildingId);
        PredictionDto dto = new PredictionDto();
        PredictionData predictionData = new PredictionData(buildingId).invoke();
        Integer month1 = predictionData.getMonth1();
        Integer month2 = predictionData.getMonth2();
        Integer month3 = predictionData.getMonth3();
        Float month1Cost = predictionData.getMonth1Cost();
        Float month2Cost = predictionData.getMonth2Cost();
        Float month3Cost = predictionData.getMonth3Cost();
        List<Bill> bills = predictionData.getBills();
        Float area = building.getArea();
        // float m1ca = month1Cost / area;
        float m1ca = month1Cost;
        // float m2ca = month2Cost / area;
        float m2ca = month2Cost;
        // float m3ca = month3Cost / area;
        float m3ca = month3Cost;

        dto.setCostYValues(Arrays.asList(ReportUtil.roundDecimal(m1ca), ReportUtil.roundDecimal(m2ca), ReportUtil.roundDecimal(m3ca)));

        dto.setSavingYValues(ReportUtil.getSavings(m1ca, m2ca, m3ca));

        int currentYear = DateUtil.getCurrentYear();

        dto.setXValues(Arrays.asList(
                ReportUtil.getDateTitle(month1, currentYear),
                ReportUtil.getDateTitle(month2, currentYear),
                ReportUtil.getDateTitle(month3, currentYear)));
        return dto;
    }

    public Float getBEScore(String buildingId, List<BillDto> billDtos) throws NotFoundException {
        //=('Reference Consumption'!C3/AF7)*100
        BuildingDto building = this.buildingService.findById(buildingId);
        float index = calculateConsumptionAreaIndex(building, billDtos) * 12;
        float beScore = (A_PLUS_CONSUMPTION / index) * 100;
        return ReportUtil.roundDecimal(beScore);
    }

    public SavingDto savingThisMonth(String buildingId) throws NotFoundException {
        Bill lastBill = billService.getLastBill(buildingId);
        BillDto lastYearBill = this.billService.filterByMonthAndYear(buildingId, lastBill.getFromMonth(),
                lastBill.getFromYear() - 1);
        SavingDto dto = new SavingDto();
        float consumption = lastBill.getTotalMonthlyConsumption() - lastYearBill.getTotalMonthlyConsumption();
        dto.setConsumption(ReportUtil.roundDecimal(consumption));
        float cost = lastBill.getTotalPayable() - lastYearBill.getTotalPayable();
        dto.setCost(ReportUtil.roundDecimal(cost));
        float environmental = lastBill.getTotalMonthlyConsumption() * CO2_CONS
                - lastYearBill.getTotalMonthlyConsumption() * CO2_CONS;
        dto.setEnvironmental(ReportUtil.roundDecimal(environmental));
        return dto;
    }

    public CostStackDto getCostStackData(String buildingId) throws NotFoundException {
        Integer currentYear = DateUtil.getCurrentYear();
        List<BillDto> dtoList = this.billService.getBillsOfYear(buildingId, currentYear, false);
        return ReportUtil.getCostStackDto(dtoList);
    }

    public CostPieDto getCostPieData(String buildingId) throws NotFoundException {
        Date currentDate = ReportUtil.getCurrentDate();
        int currentYear = DateUtil.getYear(currentDate);
        float contractedPower = 0f;
        float freeHours = 0f;
        float normalHours = 0f;
        float offHours = 0f;
        float powerInPeakHours = 0f;
        float reactivePower = 0f;
        float peakHours = 0f;
        List<BillDto> billsOfYear = billService.getBillsOfYear(buildingId, currentYear, false);

        for (BillDto billDto : billsOfYear) {
            if (billDto.getRDContractedPower() != null) {
                contractedPower += billDto.getRDContractedPower().getTotalTariffCost();
            }
            if (billDto.getAEFreeHours() != null) {
                freeHours += billDto.getAEFreeHours().getTotalTariffCost();
            }
            if (billDto.getAENormalHours() != null) {
                normalHours += billDto.getAENormalHours().getTotalTariffCost();
            }
            if (billDto.getAENormalHours() != null) {
                offHours += billDto.getAENormalHours().getTotalTariffCost();
            }
            if (billDto.getAEPeakHours() != null) {
                powerInPeakHours = billDto.getAEPeakHours().getTotalTariffCost();
            }
            if (billDto.getRDReactivePower() != null) {
                reactivePower += billDto.getRDReactivePower().getTotalTariffCost();
            }
            if (billDto.getAEPeakHours() != null) {
                peakHours += billDto.getAEPeakHours().getTotalTariffCost();
            }
        }
        CostPieDto dto = new CostPieDto();
        dto.setContractedPower(ReportUtil.roundDecimal(contractedPower / 12));
        dto.setFreeHours(ReportUtil.roundDecimal(freeHours / 12));
        dto.setNormalHours(ReportUtil.roundDecimal(normalHours / 12));
        dto.setOffHours(ReportUtil.roundDecimal(offHours / 12));
        dto.setPowerInPeakHours(ReportUtil.roundDecimal(powerInPeakHours / 12));
        dto.setReactivePower(ReportUtil.roundDecimal(reactivePower / 12));
        dto.setPeakHours(ReportUtil.roundDecimal(peakHours / 12));
        return dto;
    }

    public ConsumptionDto getConsumption(String buildingId) throws NotFoundException {
        int currentYear = DateUtil.getCurrentYear();
        List<BillDto> dtoList = this.billService.getBillsOfYear(buildingId, currentYear, false);
        ConsumptionDto dto = ReportUtil.getConsumptionDto(dtoList, currentYear, true);
        List<Float> monthConsVals = calculateBaseLine(buildingId);
        dto.setBaseLineValues(monthConsVals);
        return dto;
    }

    public ConsumptionDynamicDto getConsumptionDynamicData(String buildingId, Integer year, TimePeriodType periodType,
                                                           DatePartType datePartType) throws NotFoundException {
        boolean fillMissingBills = true;
        if (year.equals(DateUtil.getCurrentYear())) {
            fillMissingBills = false;
        }
        List<BillDto> dtoList = this.billService.getBillsOfYear(buildingId, year, fillMissingBills);
        ConsumptionDto monthlyConsDto = ReportUtil.getConsumptionDto(dtoList, year, fillMissingBills);
        // TODO calculate quarter later
        boolean isQuarter = periodType.equals(TimePeriodType.QUARTERS);
        String PEAK_HOURS = "Peak Hours";
        String OFF_HOURS = "Off Hours";
        String NORMAL_HOUR = "Normal Hour";
        String FREE_HOURS = "Free Hours";
        String PEAKHOURS_COLOR = "#ff0000";
        String OFFHOURS_COLOR = "#248f24";
        String NORMALHOURS_COLOR = "#0066cc";
        String FREEHOURS_COLOR = "#ffff00";
        switch (datePartType) {
            case FREE_HOURS:
                return !isQuarter
                        ? ReportUtil.getConsumptionDynamicDto(FREEHOURS_COLOR, FREE_HOURS, monthlyConsDto.getFreeValues())
                        : ReportUtil.getConsumptionDynamicDto(FREEHOURS_COLOR, FREE_HOURS, 9.34f, 9.32f, 10.21f, 10.56f);
            case NORMAL_HOURS:
                return !isQuarter
                        ? ReportUtil.getConsumptionDynamicDto(NORMALHOURS_COLOR, NORMAL_HOUR,
                        monthlyConsDto.getNormalValues())
                        : ReportUtil.getConsumptionDynamicDto(NORMALHOURS_COLOR, NORMAL_HOUR, 125.79f, 131.4f, 118.36f,
                        131.4f);
            case OFF_HOURS:
                return !isQuarter
                        ? ReportUtil.getConsumptionDynamicDto(OFFHOURS_COLOR, OFF_HOURS, monthlyConsDto.getOffValues())
                        : ReportUtil.getConsumptionDynamicDto(OFFHOURS_COLOR, OFF_HOURS, 265.96f, 282.73f, 238.51f,
                        250.71f);
            case PEAK_HOURS:
                return !isQuarter
                        ? ReportUtil.getConsumptionDynamicDto(PEAKHOURS_COLOR, PEAK_HOURS, monthlyConsDto.getPeakValues())
                        : ReportUtil.getConsumptionDynamicDto(PEAKHOURS_COLOR, PEAK_HOURS, 878.11f, 944.15f, 829.95f,
                        690.54f);
            default:
                throw new IllegalStateException("Unexpected value: " + datePartType);
        }
    }

    public PredictedWeatherVsRealDto getPredictedWeatherVSReal(String buildingId) throws NotFoundException {
        /*نه اونی که مشکی هست باید consumption باشه
اونی که سبزه باید سیوینگ باشه*/
        /*توی هر دوتا نمودار
مشکی ها مصرف و هزینه پارسال هستن
سبزها 5% مشکی ها*/
        /*اگه یادتون باشه این پیشبینی ها مفهومش اینه که چه اتفاقی قراره بیفته برای مصرف و هزینه
ما الان فرمول نداریم برای محاسبش
عددهای پارسال این ماه ها رو میایم تقسیم بر مساحت ساختمان میکنیم و اینجا نشون میدیم*/
        BuildingDto building = this.buildingService.findById(buildingId);
        PredictedWeatherVsRealDto dto = new PredictedWeatherVsRealDto();
        PredictionData predictionData = new PredictionData(buildingId).invoke();
        Integer month1 = predictionData.getMonth1();
        Integer month2 = predictionData.getMonth2();
        Integer month3 = predictionData.getMonth3();
        Float month1Consumption = predictionData.getMonth1Consumption();
        Float month2Consumption = predictionData.getMonth2Consumption();
        Float month3Consumption = predictionData.getMonth3Consumption();
        List<Bill> bills = predictionData.getBills();
        int billsSize = bills.size();
        Float area = building.getArea();
        float m1ca = month1Consumption;
        float m2ca = month2Consumption;
        float m3ca = month3Consumption;
        dto.setConsumptionValues(Arrays.asList(m1ca, m2ca, m3ca));
        int currentYear = DateUtil.getCurrentYear();
        dto.setXValues(Arrays.asList(ReportUtil.getDateTitle(month1, currentYear),
                ReportUtil.getDateTitle(month2, currentYear), ReportUtil.getDateTitle(month3, currentYear)));
        dto.setBaseLineValues(Arrays.asList(ReportUtil.roundDecimal(m1ca * 0.05f),
                ReportUtil.roundDecimal(m2ca * 0.05f),
                ReportUtil.roundDecimal(m3ca * 0.05f)));
        return dto;
    }

    // not needed now
    public NormalVsEEDto getNormalizedVsEnergyEfficiency() {
        int currentYear = DateUtil.getCurrentYear();
        NormalVsEEDto dto = new NormalVsEEDto();
        dto.setXValues(
                Arrays.asList("Jan-" + currentYear, "Feb-" + currentYear, "Mar-" + currentYear, "Apr-" + currentYear,
                        "May-" + currentYear, "Jun-" + currentYear, "Jul-" + currentYear, "Aug-" + currentYear,
                        "Sep-" + currentYear, "Oct-" + currentYear, "Nov-" + currentYear, "Dec-" + currentYear));
        dto.setStandardAValues(Arrays.asList(7.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f));
        dto.setStandardBValues(
                Arrays.asList(9.17f, 9.17f, 9.17f, 9.17f, 9.17f, 9.17f, 9.17f, 9.17f, 9.17f, 9.17f, 9.17f, 9.17f));
        dto.setStandardCValues(Arrays.asList(10.83f, 10.83f, 10.83f, 10.83f, 10.83f, 10.83f, 10.83f, 10.83f, 10.83f,
                10.83f, 10.83f, 10.83f));
        dto.setStandardDValues(
                Arrays.asList(12.5f, 12.5f, 12.5f, 12.5f, 12.5f, 12.5f, 12.5f, 12.5f, 12.5f, 12.5f, 12.5f, 12.5f));
        dto.setTotal(Arrays.asList(10.47f, 11.03f, 9.663f, 9.943f, 8.007f, 7.838f, 8.626f, 9.378f, 9.028f, 9.258f,
                9.496f, 11.06f));
        return dto;
    }

    public ConsumptionNormalWeatherDto getConsumptionNormalWeather(String buildingId) throws NotFoundException {
        BuildingDto building = this.buildingService.findById(buildingId);
        Integer currentYear = DateUtil.getCurrentYear();
        // TODO replace postal address with city name of address so get address of
        // building in more details
        float averageMonthlyTemperature = WeatherUtil.getAverageTemp(currentYear, building.getPostalAddress());
        List<Float> standardAVals = new ArrayList<>(Collections.nCopies(12, 0f));
        List<Float> standardBVals = new ArrayList<>(Collections.nCopies(12, 0f));
        List<Float> standardCVals = new ArrayList<>(Collections.nCopies(12, 0f));
        List<Float> standardDVals = new ArrayList<>(Collections.nCopies(12, 0f));
        List<BillDto> billsOfYear = billService.getBillsOfYear(buildingId, currentYear, false);
        for (int i = 0; i < 12; i++) {
            Integer numOfDaysOfMonth = DateUtil.getNumOfDaysOfMonth(currentYear, i + 1);
            // If the result is 0, we must put 1
            float HCDD = numOfDaysOfMonth * (COMFORT_TEMPERATURE - averageMonthlyTemperature);
            Float referenceOfMonth = (ANNUAL_EFFICIENCY_REF) / HCDD;
            Float efficiencyLevel = null;
            EnergyCertificate efficiency;
            if (billsOfYear.size() > i) {
                BillDto billDto = billsOfYear.get(i);
                Float normalizedConsumption = billDto.getTotalMonthlyConsumption() / building.getArea();
                // divide the normalized consumption indexes by the normalized reference
                // energy efficiency reference of building
                efficiencyLevel = normalizedConsumption / referenceOfMonth;
                efficiency = ReportUtil.getEnergyEfficiency(efficiencyLevel, billsOfYear);
            } else {
                efficiency = building.getEnergyCertificate();
            }
            ReportUtil.updateList(standardAVals, standardBVals, standardCVals, standardDVals, efficiencyLevel,
                    efficiency, i);
        }

        ConsumptionNormalWeatherDto dto = new ConsumptionNormalWeatherDto();
        dto.setXValues(
                Arrays.asList("Jan-" + currentYear, "Feb-" + currentYear, "Mar-" + currentYear, "Apr-" + currentYear,
                        "May-" + currentYear, "Jun-" + currentYear, "Jul-" + currentYear, "Aug-" + currentYear,
                        "Sep-" + currentYear, "Oct-" + currentYear, "Nov-" + currentYear, "Dec-" + currentYear));
        dto.setStandardAValues(standardAVals);
        dto.setStandardBValues(standardBVals);
        dto.setStandardCValues(standardCVals);
        dto.setStandardDValues(standardDVals);
        return dto;
    }

    public NormalPerCapitaDto getNormalizedPerCapita(String buildingId) throws NotFoundException {
        NormalPerCapitaDto dto = new NormalPerCapitaDto();
        Integer currentYear = DateUtil.getCurrentYear();
        List<BillDto> bills = this.billService.getBillsOfYear(buildingId, currentYear, false);
        List<Float> consumptionPerMonth = new ArrayList<>(12);
        BuildingDto building = this.buildingService.findById(buildingId);
        int numOfPeople = building.getNumberOfPeople();
        for (int i = 0; i < 12; i++) {
            if (bills.size() > i) {
                consumptionPerMonth.add(i,
                        (ReportUtil.roundDecimal(bills.get(i).getTotalMonthlyConsumption()) / numOfPeople));
            } else {
                consumptionPerMonth.add(i, 0f);
            }
        }
        dto.setXValues(
                Arrays.asList("Jan-" + currentYear, "Feb-" + currentYear, "Mar-" + currentYear, "Apr-" + currentYear,
                        "May-" + currentYear, "Jun-" + currentYear, "Jul-" + currentYear, "Aug-" + currentYear,
                        "Sept-" + currentYear, "Oct-" + currentYear, "Nov-" + currentYear, "Dec-" + currentYear));
        // The baseline is (100 kWh/cap) multiplied by the number of people in the
        // building and the constant
        // value and divided by the area of the building.
        List<Float> baseLineList = new ArrayList<>();

        // kWh/cap
        float NORM_CONS = 100f;
        Float baseLine = ReportUtil
                .roundDecimal(((NORM_CONS * building.getNumberOfPeople() * CO2_CONS) / building.getArea()));
        for (int i = 0; i < 12; i++) {
            baseLineList.add(baseLine);
        }
        dto.setTotal(consumptionPerMonth);
        dto.setBaseLine(baseLineList);
        return dto;
    }

    public CarbonSPLineDto getCarbonSPLineData(String buildingId) throws NotFoundException {
        int currentYear = DateUtil.getCurrentYear();
        List<BillDto> dtoList = this.billService.getBillsOfYear(buildingId, currentYear, false);
        BuildingDto building = buildingService.findById(buildingId);
        List<Float> totalValues = new ArrayList<>();
        for (BillDto billDto : dtoList) {
            float totalMonthlyConsumption = billDto.getTotalMonthlyConsumption();
            float total = ReportUtil.roundDecimal(
                    totalMonthlyConsumption * CO2_CONS) / building.getArea();
            totalValues.add(total);
        }
        CarbonSPLineDto dto = new CarbonSPLineDto();
        dto.setTotalValues(totalValues);
        float baseLineValue = ReportUtil
                .roundDecimal((100 * CO2_CONS * building.getNumberOfPeople()) / building.getArea());
        List<Float> baseLineValues = IntStream.range(0, 12).mapToObj(i -> baseLineValue).collect(Collectors.toList());
        dto.setBaseLineValues(baseLineValues);
        List<String> xValues = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            xValues.add(ReportUtil.getDateTitle(i, currentYear));
        }
        dto.setXValues(xValues);
        return dto;
    }

    public CarbonPieDto getCarbonPieData(String buildingId) throws NotFoundException {
        BillDto lastBill = billService.getLastBillDto(buildingId);
        BillParameterDto aeFreeHours = lastBill.getAEFreeHours();
        Float consumption = getaConsumptionOfParam(aeFreeHours);
        float producedCO2 = lastBill.getTotalMonthlyConsumption() * CO2_CONS;
        float co2Free = (consumption * CO2_CONS) / producedCO2;
        BillParameterDto aeNormalHours = lastBill.getAENormalHours();
        Float consumptionNorm = getaConsumptionOfParam(aeNormalHours);
        float co2Normal = consumptionNorm * CO2_CONS / producedCO2;
        BillParameterDto aeOffHours = lastBill.getAEOffHours();
        Float consumptionOff = getaConsumptionOfParam(aeOffHours);
        float co2Off = consumptionOff * CO2_CONS / producedCO2;
        BillParameterDto aePeakHours = lastBill.getAEPeakHours();
        Float consumptionPeak = getaConsumptionOfParam(aePeakHours);
        float co2Peak = (consumptionPeak * CO2_CONS) / producedCO2;
        CarbonPieDto dto = new CarbonPieDto();
        dto.setCo2Free(ReportUtil.roundDecimal(co2Free));
        dto.setCo2Normal(ReportUtil.roundDecimal(co2Normal));
        dto.setCo2Off(ReportUtil.roundDecimal(co2Off));
        dto.setCo2Peak(ReportUtil.roundDecimal(co2Peak));
        return dto;
    }

    // Report
    // TODO add property target field to building entity
    public Float getDefaultPropertyTarget(String buildingId, Float beScore) throws NotFoundException {
        if (beScore == null) {
            List<BillDto> billsOfYear = this.billService.getBillsOfLast12Months(buildingId);
            beScore = getBEScore(buildingId, billsOfYear);
        }
        return ReportUtil.roundDecimal(PROPERTY_TARGET_CONST * beScore);
    }

    public Float getNationalMedian() {
        // =('Reference Consumption'!C3/'Reference Consumption'!C10)*100
        float nationalMedianBEScore = (A_PLUS_CONSUMPTION / F_CONSUMPTION) * 100;
        return ReportUtil.roundDecimal(nationalMedianBEScore);
    }

    public LastMonthSummaryDto lastMonthSummary(String buildingId) throws NotFoundException {
        Bill lastBill = billService.getLastBill(buildingId);
        LastMonthSummaryDto dto = new LastMonthSummaryDto();
        float consumption = lastBill.getTotalMonthlyConsumption();
        dto.setConsumption(ReportUtil.roundDecimal(consumption));
        float cost = lastBill.getTotalPayable();
        dto.setCost(ReportUtil.roundDecimal(cost));
        float environmental = lastBill.getProducedCO2();
        dto.setEnvironmental(ReportUtil.roundDecimal(environmental));
        int lastMonth = lastBill.getFromMonth();
        dto.setLastMonth(DateUtil.getMonth(lastMonth));
        return dto;
    }

    // TODO next phase
    public void getBEBreakDown(String buildingId) {
        Float energyEfficiency;
        Float engagement;

    }

    public Float getPercentile(String buildingId) throws NotFoundException {
        Building building = this.buildingService.getById(buildingId);
        EnergyCertificate energyCertificate = building.getEnergyCertificate();
        Float rank = ReportUtil.getRankOfBuilding(energyCertificate);
        return 100f - rank;
    }

    public List<ReportIndex> getAllEnergyConsumptionIndexes(String buildingId) throws NotFoundException {
        BuildingDto buildingDto = this.buildingService.findById(buildingId);
        int currentYear = DateUtil.getCurrentYear();
        List<BillDto> lastYearBills = this.billService.getBillsOfYear(buildingId, currentYear - 1, true);
        List<BillDto> last12MonthBills = this.billService.getBillsOfLast12Months(buildingId);
        BillDto lastBillDto = this.billService.getLastBillDto(buildingId);
        ReportIndex consumptionArea = getAreaIndexConsumptionData(buildingDto, last12MonthBills, lastBillDto);
        ReportIndex consumptionCap = getCapIndexConsumptionData(buildingDto, last12MonthBills, lastBillDto);
        ReportIndex cost = getIndexCostData(buildingDto, last12MonthBills, lastBillDto);
        ReportIndex co2 = getIndexCo2Data(consumptionArea);
        ReportIndex energyEfficiencyLevel = new ReportIndex();
        //2019 :
        EnergyCertificate lastYearEnergyEfficiency = getEnergyEfficiency(buildingDto, lastYearBills);
        energyEfficiencyLevel.setBaseLineCert(lastYearEnergyEfficiency);
        //Float:
        energyEfficiencyLevel.setThisMonthCert(getEnergyEfficiency(buildingDto, last12MonthBills));
        energyEfficiencyLevel.setPropertyTargetCert(ReportUtil.increaseEnergyCertificate(lastYearEnergyEfficiency));
        energyEfficiencyLevel.setNationalMedianCert(EnergyCertificate.F);
        return Arrays.asList(consumptionArea, consumptionCap, cost, co2, energyEfficiencyLevel);
    }

    public float calculateConsumptionAreaIndex(BuildingDto building, List<BillDto> dtoList) {
        // baseLine(MonthlyAverage)=average('Consumptions and Indexes'!E7:P7)/'Meta Data'!B3
        // baseLine(MonthlyAverage)=average('Consumptions and Indexes'last12Month)/BuildingArea
        float sumOffConsumption = 0;
        for (BillDto dto : dtoList) {
            sumOffConsumption += dto.getTotalMonthlyConsumption();
        }
        float averageConsumption = sumOffConsumption / 12;
        return averageConsumption / building.getArea();
    }


    public ByteArrayInputStream getDashboardReportUrl(String buildingId) throws NotFoundException, IOException {
        BuildingDto buildingDto = this.buildingService.findById(buildingId);
        int currentYear = DateUtil.getCurrentYear();
        List<BillDto> billsOfLastYear = this.billService.getBillsOfYear(buildingId, currentYear - 1, true);
        List<BillDto> billsOfCurrentYear = this.billService.getBillsOfYear(buildingId, DateUtil.getCurrentYear(), false);
        List<BillDto> billsOfLast12Month = this.billService.getBillsOfLast12Months(buildingId);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFFont xssfFont = ExcelUtil.getXssfFont(workbook);
        CellStyle cellStyle = ExcelUtil.getCellStyle(workbook, xssfFont);
        ReportUtil.createMetaDataSheet(buildingDto, workbook, "Building Meta Data", cellStyle, xssfFont);
        ReportUtil.createReferenceSheet(workbook, xssfFont, "References");
        ReportUtil.createDataSheet(workbook, cellStyle, xssfFont, billsOfLastYear, billsOfCurrentYear, billsOfLast12Month, DataType.CONSUMPTION, "Consumptions and Indexes");
        ReportUtil.createDataSheet(workbook, cellStyle, xssfFont, billsOfLastYear, billsOfCurrentYear, billsOfLast12Month, DataType.COST, "Costs and Indexes");
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + buildingDto.getName() + ".xlsx";
        //FileOutputStream outputStream = new FileOutputStream(fileLocation);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return new ByteArrayInputStream(out.toByteArray());
    }


    public float getPossibleSolarSaving(String buildingId, int year) throws NotFoundException {
        Map<Month, Float> dailyHoursFullSunAveMap = getDailyHoursFullSunMap();
        float solarReq = getSolarReq(buildingId, year, dailyHoursFullSunAveMap);
        float possibleSavingPerYear = 0;
        for (Month month : Month.values()) {
            float savingPerDay = dailyHoursFullSunAveMap.get(month) * solarReq;
            possibleSavingPerYear += savingPerDay * 0.08 * 30;
        }
        return possibleSavingPerYear;
    }

    private float getSolarReq(String buildingId, int year, Map<Month, Float> dailyHoursFullSunAveMap) throws NotFoundException {
        List<BillDto> billsOfYear = this.billService.getBillsOfYear(buildingId, year, true);
        Map<Month, Float> consumptionPerMonth = getConsumptionPerMonths(billsOfYear);
        float powerOutputAve = 0f;
        for (Month month : consumptionPerMonth.keySet()) {
            powerOutputAve += consumptionPerMonth.get(month) / 30 / dailyHoursFullSunAveMap.get(month);
        }
        powerOutputAve = powerOutputAve / 12;
        return powerOutputAve / 0.8f;
    }

    public float getSolarReq(String buildingId, int year) throws NotFoundException {
        return getSolarReq(buildingId, year, getDailyHoursFullSunMap());

    }

    private Map<Month, Float> getDailyHoursFullSunMap() {
        Map<Month, Float> dailyHoursFullSunAveMap = new HashMap<>();
        dailyHoursFullSunAveMap.put(Month.JANUARY, 2.38f);
        dailyHoursFullSunAveMap.put(Month.FEBRUARY, 3.22f);
        dailyHoursFullSunAveMap.put(Month.MARCH, 4.67f);
        dailyHoursFullSunAveMap.put(Month.APRIL, 5.97f);
        dailyHoursFullSunAveMap.put(Month.MAY, 6.78f);
        dailyHoursFullSunAveMap.put(Month.JUNE, 7.2f);
        dailyHoursFullSunAveMap.put(Month.JULY, 7.2f);
        dailyHoursFullSunAveMap.put(Month.AUGUST, 6.68f);
        dailyHoursFullSunAveMap.put(Month.SEPTEMBER, 5.31f);
        dailyHoursFullSunAveMap.put(Month.OCTOBER, 3.78f);
        dailyHoursFullSunAveMap.put(Month.NOVEMBER, 2.56f);
        dailyHoursFullSunAveMap.put(Month.DECEMBER, 2f);
        return dailyHoursFullSunAveMap;
    }

    private Map<Month, Float> getConsumptionPerMonths(List<BillDto> billsOfYear) {
        Map<Month, Float> consumptionPerMonth = new HashMap<>();
        for (BillDto billDto : billsOfYear) {
            float sv = billDto.getAEOffHours().getConsumption();
            float vn = billDto.getAEFreeHours().getConsumption();
            float p = billDto.getAEPeakHours().getConsumption();
            float c = billDto.getAENormalHours().getConsumption();
            consumptionPerMonth.put(Month.of(billDto.getFromMonth()), (sv + vn + p + c));
        }
        return consumptionPerMonth;
    }

    //--------------------------------- Private part ---------------------------------------------

    private EnergyCertificate getEnergyEfficiency(BuildingDto buildingDto, List<BillDto> billDtos) {
        //(index/ref)*100
        float index = calculateConsumptionAreaIndex(buildingDto, billDtos) * 12;
        float lastYearEE = (index / REFERENCE) * 100;
        return ReportUtil.getEnergyEfficiency(lastYearEE, billDtos);
    }

    private ReportIndex getAreaIndexConsumptionData(BuildingDto buildingDto,
                                                    List<BillDto> billDtos,
                                                    BillDto lastBillDto) {
        logger.info("start in " + new Date());
        ReportIndex consumptionIndex = new ReportIndex();
        float baseline = calculateConsumptionAreaIndex(buildingDto, billDtos);
        consumptionIndex.setBaseline(ReportUtil.roundDecimal(baseline));
        // This Month = 'Consumptions and Indexes'!P7/'Meta Data'!B3
        consumptionIndex.setThisMonth(ReportUtil.roundDecimal((lastBillDto.getTotalMonthlyConsumption() / buildingDto.getArea())));
        consumptionIndex.setNationalMedian(12.5f);
        consumptionIndex.setPropertiesTarget(ReportUtil.roundDecimal(PROPERTY_TARGET_COEFFICIENT * baseline));
        logger.info("end in " + new Date());
        return consumptionIndex;
    }

    private ReportIndex getIndexCo2Data(ReportIndex consumptionArea) {
        ReportIndex consumptionIndex = new ReportIndex();
        float baseline = ReportUtil.roundDecimal(consumptionArea.getBaseline() * CO2_CONS);
        consumptionIndex.setBaseline(baseline);
        consumptionIndex.setThisMonth(ReportUtil.roundDecimal(consumptionArea.getThisMonth() * CO2_CONS));
        consumptionIndex.setNationalMedian(12.5f * CO2_CONS);
        consumptionIndex.setPropertiesTarget(ReportUtil.roundDecimal(PROPERTY_TARGET_COEFFICIENT * baseline));
        return consumptionIndex;
    }

    private ReportIndex getCapIndexConsumptionData(BuildingDto buildingDto,
                                                   List<BillDto> billDtos,
                                                   BillDto lastBillDto) {
        logger.info("start in " + new Date());
        ReportIndex consumptionIndex = new ReportIndex();
        float baseline = calculateConsumptionCapIndex(buildingDto, billDtos);
        consumptionIndex.setBaseline(ReportUtil.roundDecimal(baseline));
        consumptionIndex.setThisMonth(ReportUtil.roundDecimal(lastBillDto.getTotalMonthlyConsumption() / buildingDto.getNumberOfPeople()));
        consumptionIndex.setNationalMedian(null);
        consumptionIndex.setPropertiesTarget(ReportUtil.roundDecimal(PROPERTY_TARGET_COEFFICIENT * baseline));
        logger.info("end in " + new Date());
        return consumptionIndex;
    }

    private float calculateConsumptionCapIndex(BuildingDto building, List<BillDto> dtoList) {
        // =average('Consumptions and Indexes'!E7:P7)/('Meta Data'!D3+'Meta Data'!E3)
        float sumOffConsumption = 0;
        for (BillDto dto : dtoList) {
            sumOffConsumption += dto.getTotalMonthlyConsumption();
        }
        float averageConsumption = sumOffConsumption / 12;
        return averageConsumption / building.getNumberOfPeople();
    }

    private ReportIndex getIndexCostData(BuildingDto buildingDto,
                                         List<BillDto> billDtos,
                                         BillDto lastBillDto) {
        ReportIndex costIndex = new ReportIndex();
        float cnsCapBase = 0f;
        for (BillDto billDto : billDtos) {
            float totalPayable = billDto.getTotalPayable();
            cnsCapBase += totalPayable;
        }
        float index = ((cnsCapBase / 12) / buildingDto.getArea());
        costIndex.setBaseline(ReportUtil.roundDecimal(index));
        costIndex.setThisMonth(ReportUtil.roundDecimal((lastBillDto.getTotalPayable() / buildingDto.getArea())));
        costIndex.setPropertiesTarget(ReportUtil.roundDecimal(index * PROPERTY_TARGET_COEFFICIENT));
        costIndex.setNationalMedian(1.1f);
        return costIndex;
    }


    private List<Float> calculateBaseLine(String buildingId) throws NotFoundException {
        List<BillDto> lastYearBills = this.billService.getBillsOfYear(buildingId, DateUtil.getCurrentYear() - 1, true);
        List<Float> monthConsVals = new ArrayList<>(Collections.nCopies(12, 0f));
        for (BillDto lastYearBill : lastYearBills) {
            int index = lastYearBill.getFromMonth() - 1;
            monthConsVals.set(index, lastYearBill.getTotalMonthlyConsumption());
        }
        return monthConsVals;
    }

    private Float getaConsumptionOfParam(BillParameterDto parameterDto) {
        return parameterDto != null ? (parameterDto.getConsumption() != 0 ? parameterDto.getConsumption() : 1f) : 1f;
    }


    // ---------------------------------------------------Inner Classes
    // --------------------------------------------------------------
    @Getter
    private class PredictionData {
        private String buildingId;
        private Date currentDate;
        private Integer month1;
        private Integer month2;
        private Integer month3;
        private Float month1Cost;
        private Float month2Cost;
        private Float month3Cost;
        private Float month1Consumption;
        private Float month2Consumption;
        private Float month3Consumption;
        private List<Bill> bills;

        PredictionData(String buildingId) {
            this.buildingId = buildingId;
        }

        PredictionData invoke() {
            currentDate = new Date();
            Date previousYear = DateUtil.increaseDate(currentDate, -1, DateUtil.DateType.YEAR);
            List<Integer> threeNextMonths = findNextThreeMonths(previousYear);
            month1 = threeNextMonths.get(0);
            month2 = threeNextMonths.get(1);
            month3 = threeNextMonths.get(2);

            month1Cost = 0f;
            month2Cost = 0f;
            month3Cost = 0f;

            month1Consumption = 0f;
            month2Consumption = 0f;
            month3Consumption = 0f;

            bills = billService.filterByFromDateAndMonthAndBuilding(DateUtil.getYear(previousYear), month1, month2,
                    month3, buildingId);
            for (Bill bill : bills) {
                Integer fromMonth = bill.getFromMonth();
                if (fromMonth.equals(month1)) {
                    month1Cost += bill.getTotalPayable();
                    month1Consumption += bill.getTotalMonthlyConsumption();
                } else if (fromMonth.equals(month2)) {
                    month2Cost += bill.getTotalPayable();
                    month2Consumption += bill.getTotalMonthlyConsumption();
                } else if (fromMonth.equals(month3)) {
                    month3Cost += bill.getTotalPayable();
                    month3Consumption += bill.getTotalMonthlyConsumption();
                }
            }
            return this;
        }

        private List<Integer> findNextThreeMonths(Date date) {
            List<Integer> nextThreeMonths = new ArrayList<>();
            nextThreeMonths.add(DateUtil.getNextNMonth(date, 1));
            nextThreeMonths.add(DateUtil.getNextNMonth(date, 2));
            nextThreeMonths.add(DateUtil.getNextNMonth(date, 3));
            return nextThreeMonths;
        }

    }

    public static void main(String[] args) {
        Date currentDate = new Date();
        Date date = DateUtil.increaseDate(currentDate, -1, DateUtil.DateType.YEAR);
        List<Integer> nextThreeMonths = new ArrayList<>();
        nextThreeMonths.add(DateUtil.getNextNMonth(date, 1));
        nextThreeMonths.add(DateUtil.getNextNMonth(date, 2));
        nextThreeMonths.add(DateUtil.getNextNMonth(date, 3));
        System.out.println(nextThreeMonths.get(0) + "\n" + nextThreeMonths.get(1) + "\n" + nextThreeMonths.get(2));
        int currentYear = DateUtil.getCurrentYear();
        System.out.println(ReportUtil.getDateTitle(nextThreeMonths.get(0), currentYear));
        System.out.println(ReportUtil.getDateTitle(nextThreeMonths.get(1), currentYear));
        System.out.println(ReportUtil.getDateTitle(nextThreeMonths.get(2), currentYear));

    }
}
