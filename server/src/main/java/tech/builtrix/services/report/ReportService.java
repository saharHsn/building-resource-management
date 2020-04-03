package tech.builtrix.services.report;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.builtrix.enums.DatePartType;
import tech.builtrix.enums.TimePeriodType;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.models.bill.Bill;
import tech.builtrix.models.building.Building;
import tech.builtrix.models.building.EnergyCertificate;
import tech.builtrix.services.bill.BillService;
import tech.builtrix.services.building.BuildingService;
import tech.builtrix.utils.DateUtil;
import tech.builtrix.utils.ReportUtil;
import tech.builtrix.utils.WeatherUtil;
import tech.builtrix.web.dtos.bill.BillDto;
import tech.builtrix.web.dtos.bill.BillParameterDto;
import tech.builtrix.web.dtos.bill.BuildingDto;
import tech.builtrix.web.dtos.bill.EnergyConsumptionIndex;
import tech.builtrix.web.dtos.report.*;

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
    private static float COMFORT_TEMPERATURE = 20f;
    private static float ANNUAL_EFFICIENCY_REF = 51f;
    private static float CO2_CONS = 0.3332f; // kg/Kwh

    private final BillService billService;
    private final BuildingService buildingService;

    @Autowired
    public ReportService(BillService billService, BuildingService buildingService) {
        this.billService = billService;
        this.buildingService = buildingService;
    }

    public PredictionDto predict(String buildingId) {
        // 1. find next three months
        // 2. find next three months data for previous years
        // 3. average n previous year data
        // 4. return each month data
        PredictionDto dto = new PredictionDto();
        PredictionData predictionData = new PredictionData(buildingId).invoke();
        Integer month1 = predictionData.getMonth1();
        Integer month2 = predictionData.getMonth2();
        Integer month3 = predictionData.getMonth3();
        Float month1Cost = predictionData.getMonth1Cost();
        Float month2Cost = predictionData.getMonth2Cost();
        Float month3Cost = predictionData.getMonth3Cost();
        List<Bill> bills = predictionData.getBills();
        int billsSize = bills.size();
        float m1Str = ReportUtil.roundDecimal(month1Cost);
        float m2Str = ReportUtil.roundDecimal(month2Cost);
        float m3Str = ReportUtil.roundDecimal(month3Cost);
        dto.setCostYValues(Arrays.asList(m1Str, m2Str, m3Str));
        dto.setSavingYValues(ReportUtil.getSavings(month1Cost, month2Cost, month3Cost, billsSize));

        int currentYear = DateUtil.getCurrentYear();
        dto.setXValues(Arrays.asList(ReportUtil.getDateTitle(month1, currentYear),
                ReportUtil.getDateTitle(month2, currentYear), ReportUtil.getDateTitle(month3, currentYear)));
        return dto;
    }

    public Float getBEScore(String buildingId) throws NotFoundException {
        logger.info("getBEScore start --> getBillsOfYear : " + new Date());
        List<BillDto> dtoList = this.billService.getBillsOfYear(buildingId, DateUtil.getCurrentYear());
        logger.info("getBEScore end --> getBillsOfYear : " + new Date());
        logger.info("getBEScore start --> buildingService.findById : " + new Date());
        //TODO cache
        BuildingDto building = this.buildingService.findById(buildingId);
        logger.info("getBEScore end --> buildingService.findById: " + new Date());
        logger.info("getBEScore start --> getBEScore: " + new Date());
        Float beScore = this.billService.getBEScore(building, dtoList);
        logger.info("getBEScore end -->  getBEScore: " + new Date());
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
        Integer year = DateUtil.getCurrentYear();
        List<BillDto> dtoList = this.billService.getBillsOfYear(buildingId, year);
        return ReportUtil.getCostStackDto(dtoList);
    }

    public CostPieDto getCostPieData(String buildingId) throws NotFoundException {
        Date currentDate = ReportUtil.getCurrentDate();
        int year = DateUtil.getYear(currentDate);
        float contractedPower = 0f;
        float freeHours = 0f;
        float normalHours = 0f;
        float offHours = 0f;
        float powerInPeakHours = 0f;
        float reactivePower = 0f;
        float peakHours = 0f;
        List<BillDto> billsOfYear = billService.getBillsOfYear(buildingId, year);

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
        List<BillDto> dtoList = this.billService.getBillsOfYear(buildingId, currentYear);
        ConsumptionDto dto = ReportUtil.getConsumptionDto(dtoList, currentYear, true);
        List<Float> monthConsVals = calculateBaseLine(buildingId);
        dto.setBaseLineValues(monthConsVals);
        return dto;
    }

    public ConsumptionDynamicDto getConsumptionDynamicData(String buildingId, Integer year, TimePeriodType periodType,
                                                           DatePartType datePartType) throws NotFoundException {
        List<BillDto> dtoList = this.billService.getBillsOfYear(buildingId, year);
        ConsumptionDto monthlyConsDto = ReportUtil.getConsumptionDto(dtoList, year, false);
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
        dto.setConsumptionValues(Arrays.asList((month1Consumption / billsSize) / building.getArea(),
                (month2Consumption / billsSize) / building.getArea(),
                (month3Consumption / billsSize) / building.getArea()));

        int currentYear = DateUtil.getCurrentYear();
        dto.setXValues(Arrays.asList(ReportUtil.getDateTitle(month1, currentYear),
                ReportUtil.getDateTitle(month2, currentYear), ReportUtil.getDateTitle(month3, currentYear)));
        dto.setBaseLineValues(Arrays.asList(10.62f, 9.85f, 9.38f));
        return dto;
    }

    // not needed now
    public NormalVsEEDto getNormalizedVsEnergyEfficiency(String buildingId) {
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
        Float averageMonthlyTemperature = WeatherUtil.getAverageTemp(currentYear, building.getPostalAddress());
        List<Float> standardAVals = new ArrayList<>(Collections.nCopies(12, 0f));
        List<Float> standardBVals = new ArrayList<>(Collections.nCopies(12, 0f));
        List<Float> standardCVals = new ArrayList<>(Collections.nCopies(12, 0f));
        List<Float> standardDVals = new ArrayList<>(Collections.nCopies(12, 0f));
        List<BillDto> billsOfYear = billService.getBillsOfYear(buildingId, currentYear);
        for (int i = 0; i < 12; i++) {
            Integer numOfDaysOfMonth = DateUtil.getNumOfDaysOfMonth(currentYear, i + 1);
            // If the result is 0, we must put 1
            float HCDD = numOfDaysOfMonth * (COMFORT_TEMPERATURE - averageMonthlyTemperature);
            Float referenceOfMonth = (ANNUAL_EFFICIENCY_REF) / HCDD;
            Float efficiencyLevel = null;
            EnergyCertificate efficiency;
            if (billsOfYear.size() > i) {
                BillDto billDto = billsOfYear.get(i);
                Float normalizedConsumption = billDto.getTotalMonthlyConsumption() != null
                        ? billDto.getTotalMonthlyConsumption() / building.getArea()
                        : 0f;
                // divide the normalized consumption indexes by the normalized reference
                // energy efficiency reference of building
                efficiencyLevel = normalizedConsumption / referenceOfMonth;
                efficiency = ReportUtil.getEnergyEfficiency(efficiencyLevel);
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
        List<BillDto> bills = this.billService.getBillsOfYear(buildingId, currentYear);
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
        List<BillDto> dtoList = this.billService.getBillsOfYear(buildingId, currentYear);
        BuildingDto building = buildingService.findById(buildingId);
        List<Float> totalValues = new ArrayList<>();
        for (BillDto billDto : dtoList) {
            Float totalMonthlyConsumption = billDto.getTotalMonthlyConsumption();
            float total = ReportUtil.roundDecimal(
                    (totalMonthlyConsumption != null ? totalMonthlyConsumption : 1) * CO2_CONS) / building.getArea();
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
            beScore = billService.getBEScore(this.buildingService.findById(buildingId),
                    this.billService.getBillsOfYear(buildingId, DateUtil.getCurrentYear()));
        }
        return ReportUtil.roundDecimal(beScore + 0.05f * beScore);
    }

    public Float getNationalMedian(String buildingId) {
        Float nationalMedianBEScore = ((5 / 50f) * 100);
        float nationalMedian = BillService.getReference(buildingId) * 100;
        return ReportUtil.roundDecimal(nationalMedianBEScore);
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

    public List<EnergyConsumptionIndex> getAllEnergyConsumptionIndexes(String buildingId) throws NotFoundException {
        BuildingDto buildingDto = this.buildingService.findById(buildingId);
        Integer currentYear = DateUtil.getCurrentYear();
        logger.info("getBillsOfYear start: " + new Date());
        List<BillDto> billsOfYear = this.billService.getBillsOfYear(buildingId, currentYear);
        logger.info("getBillsOfYear end: " + new Date());

        logger.info("getLastBillDto start: " + new Date());
        BillDto lastBillDto = this.billService.getLastBillDto(buildingId);
        logger.info("getLastBillDto end: " + new Date());

        logger.info("getBEScore start: " + new Date());
        Float beScore = this.billService.getBEScore(this.buildingService.findById(buildingId), billsOfYear);
        logger.info("getBEScore end: " + new Date());

        Float target = getDefaultPropertyTarget(buildingId, beScore);

        logger.info("getIndexConsumptionData1 start: " + new Date());
        EnergyConsumptionIndex consumptionArea = getIndexConsumptionData(buildingDto.getArea(), buildingDto,
                billsOfYear, lastBillDto, beScore, target);
        logger.info("getIndexConsumptionData1 end: " + new Date());

        logger.info("getIndexConsumptionData2 start: " + new Date());
        EnergyConsumptionIndex consumptionCap = getIndexConsumptionData(Float.valueOf(buildingDto.getNumberOfPeople()),
                buildingDto, billsOfYear, lastBillDto, beScore, target);
        logger.info("getIndexConsumptionData2 end: " + new Date());

        logger.info("getIndexCostData start: " + new Date());
        EnergyConsumptionIndex cost = getIndexCostData(Float.valueOf(buildingDto.getNumberOfPeople()), buildingDto,
                billsOfYear, lastBillDto, beScore, target);
        logger.info("getIndexCostData start: " + new Date());
        /*
         * The energy efficiency level is calculated based on the widget shown in
         * section 4-4. You just need to visualize the average efficiency level in the
         * first column, and the energy level in the last bill in the second column. The
         * Property target is currently one level above the baseline. If the baseline is
         * C, the target is B.
         */
        EnergyConsumptionIndex energyEfficiencyLevel = new EnergyConsumptionIndex();
        Float averageMonthlyTemperature = WeatherUtil.getAverageTemp(currentYear, buildingDto.getPostalAddress());
        List<Float> monthlyEfficiencyList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Integer numOfDaysOfMonth = DateUtil.getNumOfDaysOfMonth(currentYear, i + 1);
            // If the result is 0, we must put 1
            float HCDD = numOfDaysOfMonth * (averageMonthlyTemperature - COMFORT_TEMPERATURE);
            // Average tariff consumption structure
            // Total consumption per square meter (Kwh/m2).
            // Then the calculated Kwh/m2 must be divided by HCDD value of that month.
            // efficiencyIndex = calculated Kwh/m2 / HCDD
            // This value exists in the energy certificate of the building and we have to
            // request it from users in building profile form
            // annual efficiency reference if not available 51 kwh/m2/year
            float referenceOfMonth = (ANNUAL_EFFICIENCY_REF / 12) / HCDD;
            float efficiencyLevel = 0f;
            if (billsOfYear.size() > i) {
                Float normalizedConsumption = billsOfYear.get(i).getTotalMonthlyConsumption();
                // divide the normalized consumption indexes by the normalized reference
                // energy efficiency reference of building
                efficiencyLevel = (normalizedConsumption != null ? normalizedConsumption : 0f) / referenceOfMonth;
            }
            monthlyEfficiencyList.add(efficiencyLevel);
        }
        Float sumOfEf = 0f;
        for (Float monthlyEff : monthlyEfficiencyList) {
            sumOfEf += monthlyEff;
        }
        float baseline = sumOfEf / 12;
        energyEfficiencyLevel.setBaseline(ReportUtil.roundDecimal(baseline));
        EnergyCertificate baseLineEE = ReportUtil.getEnergyEfficiency(baseline);
        energyEfficiencyLevel.setBaseLineCert(baseLineEE);
        Float efficiencyLevel = monthlyEfficiencyList.get(DateUtil.geCurrentMonth());
        energyEfficiencyLevel.setThisMonth(ReportUtil.roundDecimal(efficiencyLevel));
        energyEfficiencyLevel.setThisMonthCert(ReportUtil.getEnergyEfficiency(efficiencyLevel));
        energyEfficiencyLevel.setPropertyTargetCert(ReportUtil.getPropertyTargetCert(baseLineEE));
        energyEfficiencyLevel.setNationalMedianCert(ReportUtil.getNationalMedianCert());
        return Arrays.asList(consumptionArea, consumptionCap, cost, energyEfficiencyLevel);
    }

    // ------------------------------------private methods
    // --------------------------------------------------------------
    private EnergyConsumptionIndex getIndexConsumptionData(Float divideParam, BuildingDto buildingDto,
                                                           List<BillDto> billsOfYear, BillDto lastBillDto, Float beScore, Float target) throws NotFoundException {
        logger.info("start in " + new Date());
        EnergyConsumptionIndex consumptionIndex = new EnergyConsumptionIndex();
        float cnsCapBase = 0f;
        for (BillDto billDto : billsOfYear) {
            Float totalMonthlyConsumption = billDto.getTotalMonthlyConsumption();
            cnsCapBase += totalMonthlyConsumption != null ? totalMonthlyConsumption : 0f;
        }
        Float cnsCapNationalMedian = getCnsCapNationalMedian(divideParam, buildingDto, beScore, target,
                consumptionIndex, cnsCapBase, lastBillDto.getTotalMonthlyConsumption());
        consumptionIndex.setNationalMedian(ReportUtil.roundDecimal(cnsCapNationalMedian));
        logger.info("end in " + new Date());
        return consumptionIndex;
    }

    private Float getCnsCapNationalMedian(Float divideParam, BuildingDto buildingDto, Float beScore, Float target,
                                          EnergyConsumptionIndex consumptionIndex, Float cnsCapBase, Float totalMonthlyConsumption2)
            throws NotFoundException {
        cnsCapBase = (cnsCapBase / 12) / buildingDto.getArea();
        consumptionIndex.setBaseline(cnsCapBase);
        String buildingId = buildingDto.getId();
        Float cnsAreaLastMonth = (totalMonthlyConsumption2 != null ? totalMonthlyConsumption2 : 0) / divideParam;
        consumptionIndex.setThisMonth(cnsAreaLastMonth);
        // TODO ask if propTarget is calculated correctly
        float cnsCapPropsTarget = (cnsCapBase * beScore) / target;
        consumptionIndex.setPropertiesTarget(ReportUtil.roundDecimal(cnsCapPropsTarget));
        Float nationalMedian = getNationalMedian(buildingId);
        return cnsCapBase * (beScore / nationalMedian);
    }

    private EnergyConsumptionIndex getIndexCostData(Float divideParam, BuildingDto buildingDto,
                                                    List<BillDto> billsOfYear, BillDto lastBillDto, Float beScore, Float target) throws NotFoundException {
        EnergyConsumptionIndex consumptionIndex = new EnergyConsumptionIndex();
        float cnsCapBase = 0f;
        for (BillDto billDto : billsOfYear) {
            Float totalPayable = billDto.getTotalPayable();
            cnsCapBase += totalPayable != null ? totalPayable : 0;
        }
        Float cnsCapNationalMedian = getCnsCapNationalMedian(divideParam, buildingDto, beScore, target,
                consumptionIndex, cnsCapBase, lastBillDto.getTotalPayable());
        consumptionIndex.setNationalMedian(ReportUtil.roundDecimal(cnsCapNationalMedian));

        return consumptionIndex;
    }

    private List<Float> calculateBaseLine(String buildingId) throws NotFoundException {
        List<BillDto> lastYearBills = this.billService.getBillsOfYear(buildingId, DateUtil.getCurrentYear() - 1);
        List<Float> monthConsVals = new ArrayList<>(Collections.nCopies(12, 0f));
        for (BillDto lastYearBill : lastYearBills) {
            int index = lastYearBill.getFromMonth() - 1;
            monthConsVals.set(index, lastYearBill.getTotalMonthlyConsumption());
        }
        return monthConsVals;
    }

    private Float getaConsumptionOfParam(BillParameterDto parameterDto) {
        return parameterDto != null ? (parameterDto.getConsumption() != null ? parameterDto.getConsumption() : 1f) : 1f;
    }

    public CurrentMonthSummaryDto currentMonthSummary(String buildingId) throws NotFoundException {
        Bill lastBill = billService.getLastBill(buildingId);
        CurrentMonthSummaryDto dto = new CurrentMonthSummaryDto();
        float consumption = lastBill.getTotalMonthlyConsumption();
        dto.setConsumption(ReportUtil.roundDecimal(consumption));
        float cost = lastBill.getTotalPayable();
        dto.setCost(ReportUtil.roundDecimal(cost));
        float environmental = lastBill.getProducedCO2();
        dto.setEnvironmental(ReportUtil.roundDecimal(environmental));
        return dto;
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
