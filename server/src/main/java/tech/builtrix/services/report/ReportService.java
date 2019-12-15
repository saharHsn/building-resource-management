package tech.builtrix.services.report;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.builtrix.dtos.bill.BillDto;
import tech.builtrix.dtos.bill.BillParameterDto;
import tech.builtrix.dtos.bill.BuildingDto;
import tech.builtrix.dtos.bill.EnergyConsumptionIndex;
import tech.builtrix.dtos.report.*;
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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created By sahar at 12/4/19
 */
@Component
@Slf4j
public class ReportService {
    private static String FREEHOURS_COLOR = "#ffff00";
    private static String NORMALHOURS_COLOR = "#0066cc";
    private static String OFFHOURS_COLOR = "#248f24";
    private static String PEAKHOURS_COLOR = "#ff0000";
    private static String FREE_HOURS = "Free Hours";
    private static String NORMAL_HOUR = "Normal Hour";
    private static String OFF_HOURS = "Off Hours";
    private static String PEAK_HOURS = "Peak Hours";

    // private static float kg_CO2_per_each_kWh = 0.408f;
    private static float COMFORT_TEMPERATURE = 20f;
    private static float ANNUAL_EFFICIENCY_REF = 51f;
    private static float CO2_CONS = 0.3332f; //kg/Kwh
    private static float NORM_CONS = 100f; //kWh/cap

    private final BillService billService;
    private final BuildingService buildingService;

    @Autowired
    public ReportService(BillService billService, BuildingService buildingService) {
        this.billService = billService;
        this.buildingService = buildingService;
    }

    public PredictionDto predict(String buildingId) {
        //1. find next three months
        //2. find next three months data for previous years
        //3. average n previous year data
        //4. return each month data
        PredictionDto dto = new PredictionDto();
        PredictionData predictionData = new PredictionData(buildingId).invoke();
        Integer month1 = predictionData.getMonth1();
        Integer month2 = predictionData.getMonth2();
        Integer month3 = predictionData.getMonth3();
        Float month1Cost = predictionData.getMonth1Cost();
        Float month2Cost = predictionData.getMonth2Cost();
        Float month3Cost = predictionData.getMonth3Cost();
        List<Bill> bills = predictionData.getBills();
        // dto.setCostYValues(Arrays.asList(6135.5f, 7130.4f, 6234.3f));
        int billsSize = bills.size();
        dto.setCostYValues(Arrays.asList(month1Cost / billsSize, month2Cost / billsSize, month3Cost / billsSize));
        // dto.setSavingYValues(Arrays.asList(321f, 420f, 360f));
        dto.setSavingYValues(ReportUtil.getSavings(month1Cost, month2Cost, month3Cost, billsSize));
        //dto.setXValues(Arrays.asList("Oct-2019", "Nov-2019", "Dec-2019"));

        int currentYear = DateUtil.getCurrentYear();
        dto.setXValues(Arrays.asList(
                ReportUtil.getDateTitle(month1, currentYear),
                ReportUtil.getDateTitle(month2, currentYear),
                ReportUtil.getDateTitle(month3, currentYear)));
        return dto;
    }

    public Float getBEScore(String buildingId) throws NotFoundException {
        logger.info("getBEScore start --> getBillsOfYear : " + new Date());
        List<BillDto> dtoList = this.billService.getBillsOfYear(buildingId, DateUtil.getCurrentYear());
        logger.info("getBEScore end --> getBillsOfYear : " + new Date());
        logger.info("getBEScore start --> buildingService.findById : " + new Date());
        BuildingDto building = this.buildingService.findById(buildingId);
        logger.info("getBEScore end --> buildingService.findById: " + new Date());
        logger.info("getBEScore start --> getBEScore: " + new Date());
        Float beScore = this.billService.getBEScore(building, dtoList);
        logger.info("getBEScore end -->  getBEScore: " + new Date());
        return beScore;
    }

    public SavingDto savingThisMonth(String buildingId) throws NotFoundException {
        Bill lastBill = billService.getLastBill(buildingId);

        PreviousYearsParamData previousYearsParamData = new PreviousYearsParamData(getAllPreviousBills(buildingId)).invoke();

        Integer billSize = previousYearsParamData.getBillSize();
        Float consumptionSum = previousYearsParamData.getConsumptionSum();
        Float costSum = previousYearsParamData.getCostSum();
        Float environmentSum = previousYearsParamData.getEnvironmentSum();
        SavingDto dto = new SavingDto();
        dto.setConsumption(lastBill.getTotalMonthlyConsumption() - (consumptionSum / billSize));
        dto.setCost(lastBill.getTotalPayable() - (costSum / billSize));
        dto.setEnvironmental(lastBill.getTotalMonthlyConsumption() * CO2_CONS - (environmentSum / billSize));
        /*dto.setConsumption(1263f);
        dto.setCost(190f);
        dto.setEnvironmental(515f);*/
        return dto;
    }

    public CostStackDto getCostStackData(String buildingId) throws NotFoundException {
        Integer year = DateUtil.getCurrentYear();
        List<BillDto> dtoList = this.billService.getBillsOfYear(buildingId, 2018);
        /* dto.setContractedPowerValues(Arrays.asList(833.69f, 846.81f, 739.59f, 896.43f, 684.86f, 672.34f, 742.91f, 782.83f, 753.81f, 762.87f, 714.73f, 786.84f));
        dto.setFreeValues(Arrays.asList(437.62f, 402.36f, 467.34f, 460.2f, 369.59f, 414.66f, 368.28f, 406.59f, 453.63f, 397.01f, 427.34f, 556.36f));
        dto.setOffValues(Arrays.asList(265.96f, 282.73f, 238.51f, 250.71f, 211.15f, 209.45f, 209f, 228.47f, 227.03f, 220.12f, 245.33f, 310.36f));
        dto.setPeakValues(Arrays.asList(878.11f, 944.15f, 829.95f, 690.54f, 473.42f, 429.33f, 513.55f, 547.49f, 481.35f, 527.35f, 695.46f, 865.32f));
        dto.setPowerInPeakValues(Arrays.asList(833.69f, 846.81f, 739.59f, 896.43f, 684.86f, 672.34f, 742.91f, 782.83f, 753.81f, 762.87f, 714.73f, 786.84f));
        dto.setNormalValues(Arrays.asList(1844.74f, 1932.34f, 1715.54f, 1833.96f, 1538.25f, 1451.76f, 1729.1f, 878.35f, 1743.17f, 1881.22f, 1728.28f, 1834.11f));
        dto.setReactivePowerValues(Arrays.asList(9.34f, 9.32f, 10.21f, 10.56f, 13.36f, 16.35f, 9.78f, 6.43f, 6.4f, 9.37f, 12.25f, 6.02f));*/
        return ReportUtil.getCostStackDto(dtoList);
    }

    public CostPieDto getCostPieData(String buildingId) throws NotFoundException {
        Date currentDate = ReportUtil.getCurrentDate();
        int year = DateUtil.getYear(currentDate);
        List<BillDto> dtoList = new ArrayList<>();
        float contractedPower = 0f;
        float freeHours = 0f;
        float normalHours = 0f;
        float offHours = 0f;
        float powerInPeakHours = 0f;
        float reactivePower = 0f;
        float peakHours = 0f;
        List<BillDto> billsOfYear = billService.getBillsOfYear(buildingId, 2018);
        /*for (int i = 0; i < 12; i++) {
            BillDto billDto = billService.filterByMonthAndYear(buildingId, i, 2018);
            dtoList.add(billDto);
        }*/
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
        dto.setContractedPower(contractedPower / 12);
        dto.setFreeHours(freeHours / 12);
        dto.setNormalHours(normalHours / 12);
        dto.setOffHours(offHours / 12);
        dto.setPowerInPeakHours(powerInPeakHours / 12);
        dto.setReactivePower(reactivePower / 12);
        dto.setPeakHours(peakHours / 12);
        return dto;
    }

    public ConsumptionDto getConsumption(String buildingId) throws NotFoundException {
        List<BillDto> dtoList = this.billService.getBillsOfYear(buildingId, DateUtil.getCurrentYear());
        ConsumptionDto dto = ReportUtil.getConsumptionDto(dtoList);
        /*dto.setContractedPowerValues(Arrays.asList(125.79f, 131.4f, 118.36f, 131.4f, 126.81f, 131.04f, 126.81f, 131.04f, 131.04f, 131.99f, 136.69f, 131.99f));
        dto.setPowerInPeakValues(Arrays.asList(833.69f, 846.81f, 739.59f, 896.43f, 684.86f, 672.34f, 742.91f, 782.83f, 753.81f, 762.87f, 714.73f, 786.84f));
        dto.setReactivePowerValues(Arrays.asList(9.34f, 9.32f, 10.21f, 10.56f, 13.36f, 16.35f, 9.78f, 6.43f, 6.4f, 9.37f, 12.25f, 6.02f));
        dto.setNormalValues(Arrays.asList(1844.74f, 1932.34f, 1715.54f, 1833.96f, 1538.25f, 1451.76f, 1729.1f, 878.35f, 1743.17f, 1881.22f, 1728.28f, 1834.11f));
        dto.setPeakValues(Arrays.asList(878.11f, 944.15f, 829.95f, 690.54f, 473.42f, 429.33f, 513.55f, 547.49f, 481.35f, 527.35f, 695.46f, 865.32f));
        dto.setFreeValues(Arrays.asList(437.62f, 402.36f, 467.34f, 460.2f, 369.59f, 414.66f, 368.28f, 406.59f, 453.63f, 397.01f, 427.34f, 556.36f));
        dto.setOffValues(Arrays.asList(265.96f, 282.73f, 238.51f, 250.71f, 211.15f, 209.45f, 209f, 228.47f, 227.03f, 220.12f, 245.33f, 310.36f));*/
        /*This baseline is now calculated using the data of the previous years
        (we are comparing the monthly consumption of the building with the same months of the previous year).
         */
        List<Float> monthConsVals = calculateBaseLine(buildingId);
        //dto.setBaseLineValues(Arrays.asList(1844.74f, 1932.34f, 1715.54f, 1833.96f, 1538.25f, 1451.76f, 1729.1f, 878.35f, 1743.17f, 1881.22f, 1728.28f, 1834.11f));
        dto.setBaseLineValues(monthConsVals);
        return dto;
    }

    public ConsumptionDynamicDto getConsumptionDynamicData(String buildingId,
                                                           Integer year,
                                                           TimePeriodType periodType,
                                                           DatePartType datePartType) throws NotFoundException {
        List<BillDto> dtoList = this.billService.getBillsOfYear(buildingId, year);
        ConsumptionDto monthlyConsDto = ReportUtil.getConsumptionDto(dtoList);
        //TODO calculate quarter later
        boolean isQuarter = periodType.equals(TimePeriodType.QUARTERS);
        switch (datePartType) {
            case FREE_HOURS:
                return !isQuarter ? ReportUtil.getConsumptionDynamicDto(FREEHOURS_COLOR, FREE_HOURS, monthlyConsDto.getFreeValues()) : ReportUtil.getConsumptionDynamicDto(FREEHOURS_COLOR, FREE_HOURS, 9.34f, 9.32f, 10.21f, 10.56f);
            case NORMAL_HOURS:
                return !isQuarter ? ReportUtil.getConsumptionDynamicDto(NORMALHOURS_COLOR, NORMAL_HOUR, monthlyConsDto.getNormalValues()) : ReportUtil.getConsumptionDynamicDto(NORMALHOURS_COLOR, NORMAL_HOUR, 125.79f, 131.4f, 118.36f, 131.4f);
            case OFF_HOURS:
                return !isQuarter ? ReportUtil.getConsumptionDynamicDto(OFFHOURS_COLOR, OFF_HOURS, monthlyConsDto.getOffValues()) : ReportUtil.getConsumptionDynamicDto(OFFHOURS_COLOR, OFF_HOURS, 265.96f, 282.73f, 238.51f, 250.71f);
            case PEAK_HOURS:
                return !isQuarter ? ReportUtil.getConsumptionDynamicDto(PEAKHOURS_COLOR, PEAK_HOURS, monthlyConsDto.getPeakValues()) : ReportUtil.getConsumptionDynamicDto(PEAKHOURS_COLOR, PEAK_HOURS, 878.11f, 944.15f, 829.95f, 690.54f);
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
        // dto.setCostYValues(Arrays.asList(6135.5f, 7130.4f, 6234.3f));
        int billsSize = bills.size();
        dto.setConsumptionValues(Arrays.asList((month1Consumption / billsSize) / building.getArea(),
                (month2Consumption / billsSize) / building.getArea(),
                (month3Consumption / billsSize) / building.getArea()));

        //dto.setXValues(Arrays.asList("Jan-2019", "Feb-2019", "Mar-2019"));
        int currentYear = DateUtil.getCurrentYear();
        dto.setXValues(Arrays.asList(
                ReportUtil.getDateTitle(month1, currentYear),
                ReportUtil.getDateTitle(month2, currentYear),
                ReportUtil.getDateTitle(month3, currentYear)));
        dto.setBaseLineValues(Arrays.asList(10.62f, 9.85f, 9.38f));
        // dto.setConsumptionValues(Arrays.asList(10.94f, 11.21f, 9.3f));
        return dto;
    }

    //not needed now
    public NormalVsEEDto getNormalizedVsEnergyEfficiency(String buildingId) {
        NormalVsEEDto dto = new NormalVsEEDto();
        dto.setXValues(Arrays.asList("Jan-2018",
                "Feb-2018",
                "Mar-2018",
                "Apr-2018",
                "May-2018",
                "Jun-2018",
                "Jul-2018",
                "Aug-2018",
                "Sep-2018",
                "Oct-2018",
                "Nov-2018",
                "Dec-2018"));
        dto.setStandardAValues(Arrays.asList(7.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f, 7.5f));
        dto.setStandardBValues(Arrays.asList(9.17f, 9.17f, 9.17f, 9.17f, 9.17f, 9.17f, 9.17f, 9.17f, 9.17f, 9.17f, 9.17f, 9.17f));
        dto.setStandardCValues(Arrays.asList(10.83f, 10.83f, 10.83f, 10.83f, 10.83f, 10.83f, 10.83f, 10.83f, 10.83f, 10.83f, 10.83f, 10.83f));
        dto.setStandardDValues(Arrays.asList(12.5f, 12.5f, 12.5f, 12.5f, 12.5f, 12.5f, 12.5f, 12.5f, 12.5f, 12.5f, 12.5f, 12.5f));
        dto.setTotal(Arrays.asList(10.47f, 11.03f, 9.663f, 9.943f, 8.007f, 7.838f, 8.626f, 9.378f, 9.028f, 9.258f, 9.496f, 11.06f));
        return dto;
    }

    public ConsumptionNormalWeatherDto getConsumptionNormalWeather(String buildingId) throws NotFoundException {
        BuildingDto building = this.buildingService.findById(buildingId);
        Integer currentYear = DateUtil.getCurrentYear();
        //TODO replace postal address with city name of address so get address of building in more details
        Float averageMonthlyTemperature = WeatherUtil.getAverageTemp(currentYear,
                building.getPostalAddress());
        List<Float> standardAVals = new ArrayList<>(Collections.nCopies(12, 0f));
        List<Float> standardBVals = new ArrayList<>(Collections.nCopies(12, 0f));
        List<Float> standardCVals = new ArrayList<>(Collections.nCopies(12, 0f));
        List<Float> standardDVals = new ArrayList<>(Collections.nCopies(12, 0f));
        List<BillDto> billsOfYear = billService.getBillsOfYear(buildingId, 2018);
        for (int i = 0; i < 12; i++) {
            Integer numOfDaysOfMonth = DateUtil.getNumOfDaysOfMonth(currentYear, i + 1);
            // If the result is 0, we must put 1
            float HCDD = numOfDaysOfMonth * (COMFORT_TEMPERATURE - averageMonthlyTemperature);
            Float referenceOfMonth = (ANNUAL_EFFICIENCY_REF) / HCDD;
            Float efficiencyLevel = null;
            EnergyCertificate efficiency;
            if (billsOfYear.size() > i) {
                BillDto billDto = billsOfYear.get(i);
                Float normalizedConsumption = billDto.getTotalMonthlyConsumption() != null ? billDto.getTotalMonthlyConsumption() / building.getArea() : 0f;
                //divide the normalized consumption indexes by the normalized reference
                //energy efficiency reference of building
                efficiencyLevel = normalizedConsumption / referenceOfMonth;
                efficiency = ReportUtil.getEnergyEfficiency(efficiencyLevel);
            } else {
                efficiency = building.getEnergyCertificate();
            }
            ReportUtil.updateList(standardAVals, standardBVals, standardCVals, standardDVals, efficiencyLevel, efficiency, i);
        }

        ConsumptionNormalWeatherDto dto = new ConsumptionNormalWeatherDto();
        dto.setXValues(Arrays.asList("Jan-" + currentYear,
                "Feb-" + currentYear,
                "Mar-" + currentYear,
                "Apr-" + currentYear,
                "May-" + currentYear,
                "Jun-" + currentYear,
                "Jul-" + currentYear,
                "Aug-" + currentYear,
                "Sep-" + currentYear,
                "Oct-" + currentYear,
                "Nov-" + currentYear,
                "Dec-" + currentYear));
        //dto.setStandardAValues(Arrays.asList(0f, 0f, 0f, 0.042f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f));
        dto.setStandardAValues(standardAVals);
        //dto.setStandardBValues(Arrays.asList(0f, 0f, 0f, 0f, 0.07f, 0.092f, 0f, 0.08f, 0.067f, 0f, 0f, 0f));
        dto.setStandardBValues(standardBVals);
        //dto.setStandardCValues(Arrays.asList(0.04f, 0.04f, 0.045f, 0f, 0f, 0f, 0.14f, 0f, 0f, 0.092f, 0.066f, 0.05f));
        dto.setStandardCValues(standardCVals);
        //dto.setStandardDValues(Arrays.asList(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f));
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
            //Date date = DateUtil.getCustomDate(currentYear, i);
            //Integer numOfPeople = building.getNumOfPeopleMap().get(date);
            if (bills.size() > i) {
                consumptionPerMonth.add(i, (bills.get(i).getTotalMonthlyConsumption()) / numOfPeople);
            } else {
                consumptionPerMonth.add(i, 0f);
            }
        }
        dto.setXValues(Arrays.asList("Jan-2018", "Feb-2018", "Mar-2018", "Apr-2018", "May-2018", "Jun-2018", "Jul-2018", "Aug-2018", "Sept-2018", "Oct-2018", "Nov-2018", "Dec-2018"));
        //The baseline is (100 kWh/cap) multiplied by the number of people in the building and the constant
        // value and divided by the area of the building.
        List<Float> baseLineList = new ArrayList<>();

        Float baseLine = (NORM_CONS * building.getNumberOfPeople() * CO2_CONS) / building.getArea();
        for (int i = 0; i < 12; i++) {
            baseLineList.add(baseLine);
        }
        //dto.setBaseLine(Arrays.asList(98.13f, 98.13f, 98.13f, 98.13f, 98.13f, 98.13f, 98.13f, 98.13f, 98.13f, 98.13f, 98.13f, 98.13f));
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
            totalValues.add(((totalMonthlyConsumption != null ? totalMonthlyConsumption : 1) * CO2_CONS) / building.getArea());
        }
        // ConsumptionDto dto = getConsumptionDto(dtoList);
        CarbonSPLineDto dto = new CarbonSPLineDto();
        // dto.setTotalValues(Arrays.asList(4.28f, 4.51f, 3.95f, 4.07f, 3.27f, 3.2f, 3.53f, 3.83f, 3.69f, 3.78f, 3.88f, 4.52f));
        dto.setTotalValues(totalValues);
        float baseLineValue = (100 * CO2_CONS * building.getNumberOfPeople()) / building.getArea();
        // dto.setBaseLineValues(Arrays.asList(3.74f, 3.74f, 3.74f, 3.74f, 3.74f, 3.74f, 3.74f, 3.74f, 3.74f, 3.74f, 3.74f, 3.74f));
        List<Float> baseLineValues = IntStream.range(0, 12).mapToObj(i -> baseLineValue).collect(Collectors.toList());
        dto.setBaseLineValues(baseLineValues);
        List<String> xValues = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            xValues.add(ReportUtil.getDateTitle(i, currentYear));
        }
        dto.setXValues(xValues);
       /* dto.setXValues(Arrays.asList("Jan-2018",
                "Feb-2018",
                "Mar-2018",
                "Apr-2018",
                "May-2018",
                "Jun-2018",
                "Jul-2018",
                "Aug-2018",
                "Sep-2018",
                "Oct-2018",
                "Nov-2018",
                "Dec-2018"));*/
        return dto;
    }

    public CarbonPieDto getCarbonPieData(String buildingId) throws NotFoundException {
        BillDto lastBill = billService.getLastBillDto(buildingId);
        BillParameterDto aeFreeHours = lastBill.getAEFreeHours();
        Float consumption = getaConsumptionOfParam(aeFreeHours);
        // Float producedCO2 = lastBill.getProducedCO2();
        Float producedCO2 = lastBill.getTotalMonthlyConsumption() * CO2_CONS;
        Float co2Free = (consumption * CO2_CONS) / producedCO2;
        BillParameterDto aeNormalHours = lastBill.getAENormalHours();
        Float consumptionNorm = getaConsumptionOfParam(aeNormalHours);
        Float co2Normal = consumptionNorm * CO2_CONS / producedCO2;
        BillParameterDto aeOffHours = lastBill.getAEOffHours();
        Float consumptionOff = getaConsumptionOfParam(aeOffHours);
        Float co2Off = consumptionOff * CO2_CONS / producedCO2;
        BillParameterDto aePeakHours = lastBill.getAEPeakHours();
        Float consumptionPeak = getaConsumptionOfParam(aePeakHours);
        Float co2Peak = (consumptionPeak * CO2_CONS) / producedCO2;
        CarbonPieDto dto = new CarbonPieDto();
        dto.setCo2Free(co2Free);
        dto.setCo2Normal(co2Normal);
        dto.setCo2Off(co2Off);
        dto.setCo2Peak(co2Peak);
        /*dto.setCo2Free(19.4f);
        dto.setCo2Normal(51f);
        dto.setCo2Off(11.4f);
        dto.setCo2Peak(18.1f);*/
        return dto;
    }

    private Float getaConsumptionOfParam(BillParameterDto aeFreeHours) {
        return aeFreeHours != null ? (aeFreeHours.getConsumption() != null ? aeFreeHours.getConsumption() : 1f) : 1f;
    }

    //Report
    //TODO add property target field to building entity
    public Float getDefaultPropertyTarget(String buildingId, Float beScore) throws NotFoundException {
        if (beScore == null) {
            beScore = billService.getBEScore(this.buildingService.findById(buildingId), this.billService.getBillsOfYear(buildingId, DateUtil.getCurrentYear()));
        }
        return beScore + 0.05f * beScore;
    }

    public Float getNationalMedian(String buildingId) throws NotFoundException {
        Float nationalMedianBEScore = (5 / 50f * 100);
        Float nationalMedian = BillService.getReference(buildingId) * 100;
        return nationalMedian;
    }

    //TODO next phase
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
        EnergyConsumptionIndex consumptionArea = getIndexConsumptionData(buildingDto.getArea(), buildingDto, billsOfYear, lastBillDto, beScore, target);
        logger.info("getIndexConsumptionData1 end: " + new Date());

        logger.info("getIndexConsumptionData2 start: " + new Date());
        EnergyConsumptionIndex consumptionCap = getIndexConsumptionData(Float.valueOf(buildingDto.getNumberOfPeople()), buildingDto, billsOfYear, lastBillDto, beScore, target);
        logger.info("getIndexConsumptionData2 end: " + new Date());

        logger.info("getIndexCostData start: " + new Date());
        EnergyConsumptionIndex cost = getIndexCostData(Float.valueOf(buildingDto.getNumberOfPeople()), buildingDto, billsOfYear, lastBillDto, beScore, target);
        logger.info("getIndexCostData start: " + new Date());
        /*The energy efficiency level is calculated based on the widget shown in section 4-4.
         You just need to visualize the average efficiency level in the first column,
         and the energy level in the last bill in the second column.
         The Property target is currently one level above the baseline.
         If the baseline is C, the target is B.
         */
        EnergyConsumptionIndex energyEfficiencyLevel = new EnergyConsumptionIndex();
        Float averageMonthlyTemperature = WeatherUtil.getAverageTemp(currentYear,
                buildingDto.getPostalAddress());
        List<Float> monthlyEfficiencyList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Integer numOfDaysOfMonth = DateUtil.getNumOfDaysOfMonth(currentYear, i + 1);
            // If the result is 0, we must put 1
            float HCDD = numOfDaysOfMonth * (averageMonthlyTemperature - COMFORT_TEMPERATURE);
            // Average tariff consumption structure
            // Total consumption per square meter (Kwh/m2).
            //Then the calculated Kwh/m2 must be divided by HCDD value of that month.
            //efficiencyIndex = calculated Kwh/m2 / HCDD
            //This value exists in the energy certificate of the building and we have to request it from users in building profile form
            //annual efficiency reference if not available  51 kwh/m2/year
            float referenceOfMonth = (ANNUAL_EFFICIENCY_REF / 12) / HCDD;
            Float efficiencyLevel = 0f;
            if (billsOfYear.size() > i) {
                Float normalizedConsumption = billsOfYear.get(i).getTotalMonthlyConsumption();
                //divide the normalized consumption indexes by the normalized reference
                //energy efficiency reference of building
                efficiencyLevel = (normalizedConsumption != null ? normalizedConsumption : 0f) / referenceOfMonth;
            }
            monthlyEfficiencyList.add(efficiencyLevel);
        }
        Float sumOfEf = 0f;
        for (Float monthlyEff : monthlyEfficiencyList) {
            sumOfEf += monthlyEff;
        }
        float baseline = sumOfEf / 12;
        energyEfficiencyLevel.setBaseline(baseline);
        EnergyCertificate baseLineEE = ReportUtil.getEnergyEfficiency(baseline);
        energyEfficiencyLevel.setBaseLineCert(baseLineEE);
        Float efficiencyLevel = monthlyEfficiencyList.get(DateUtil.geCurrentMonth());
        energyEfficiencyLevel.setThisMonth(efficiencyLevel);
        energyEfficiencyLevel.setThisMonthCert(ReportUtil.getEnergyEfficiency(efficiencyLevel));
        energyEfficiencyLevel.setPropertyTargetCert(ReportUtil.getPropertyTargetCert(baseLineEE));
        energyEfficiencyLevel.setNationalMedianCert(ReportUtil.getNationalMedianCert());
        return Arrays.asList(consumptionArea, consumptionCap, cost, energyEfficiencyLevel);
    }

    //------------------------------------private methods --------------------------------------------------------------
    private EnergyConsumptionIndex getIndexConsumptionData(Float divideParam,
                                                           BuildingDto buildingDto,
                                                           List<BillDto> billsOfYear,
                                                           BillDto lastBillDto,
                                                           Float beScore,
                                                           Float target) throws NotFoundException {
        logger.info("start in " + new Date());
        EnergyConsumptionIndex consumptionIndex = new EnergyConsumptionIndex();
        Float cnsCapBase = 0f;
        for (BillDto billDto : billsOfYear) {
            Float totalMonthlyConsumption = billDto.getTotalMonthlyConsumption();
            cnsCapBase += totalMonthlyConsumption != null ? totalMonthlyConsumption : 0f;
        }
        cnsCapBase = (cnsCapBase / 12) / buildingDto.getArea();
        consumptionIndex.setBaseline(cnsCapBase);
        String buildingId = buildingDto.getId();
        Float totalMonthlyConsumption = lastBillDto.getTotalMonthlyConsumption();
        Float cnsAreaLastMonth = (totalMonthlyConsumption != null ? totalMonthlyConsumption : 0) / divideParam;
        consumptionIndex.setThisMonth(cnsAreaLastMonth);
        //TODO ask if propTarget is calculated correctly
        float cnsCapPropsTarget = (cnsCapBase * beScore) / target;
        consumptionIndex.setPropertiesTarget(cnsCapPropsTarget);
        Float nationalMedian = getNationalMedian(buildingId);
        Float cnsCapNationalMedian = cnsCapBase * (beScore / nationalMedian);
        consumptionIndex.setNationalMedian(cnsCapNationalMedian);
        logger.info("end in " + new Date());
        return consumptionIndex;
    }

    private EnergyConsumptionIndex getIndexCostData(Float divideParam,
                                                    BuildingDto buildingDto,
                                                    List<BillDto> billsOfYear, BillDto lastBillDto, Float beScore, Float target) throws NotFoundException {
        EnergyConsumptionIndex consumptionIndex = new EnergyConsumptionIndex();
        Float cnsCapBase = 0f;
        for (BillDto billDto : billsOfYear) {
            Float totalPayable = billDto.getTotalPayable();
            cnsCapBase += totalPayable != null ? totalPayable : 0;
        }
        cnsCapBase = (cnsCapBase / 12) / buildingDto.getArea();
        consumptionIndex.setBaseline(cnsCapBase);
        String buildingId = buildingDto.getId();
        Float totalPayable = lastBillDto.getTotalPayable();
        Float cnsAreaLastMonth = (totalPayable != null ? totalPayable : 0) / divideParam;
        consumptionIndex.setThisMonth(cnsAreaLastMonth);

        float cnsCapPropsTarget = (cnsCapBase * beScore) / target;
        consumptionIndex.setPropertiesTarget(cnsCapPropsTarget);

        Float nationalMedian = getNationalMedian(buildingId);
        Float cnsCapNationalMedian = cnsCapBase * (beScore / nationalMedian);
        consumptionIndex.setNationalMedian(cnsCapNationalMedian);

        return consumptionIndex;
    }

    private List<Float> calculateBaseLine(String buildingId) throws NotFoundException {
        List<BillDto> lastYearBills = this.billService.getBillsOfYear(buildingId, DateUtil.getCurrentYear() - 1);
        List<Float> monthConsVals = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            if (lastYearBills.size() > i) {
                monthConsVals.add(i, lastYearBills.get(i).getTotalMonthlyConsumption());
            }
        }
        return monthConsVals;
    }

    private List<Bill> getAllPreviousBills(String buildingId) {
        Date currentDate = new Date();
        Date previousYear = DateUtil.increaseDate(currentDate, -1, DateUtil.DateType.YEAR);
        List<Bill> bills = this.billService.filterByFromDateAndMonthAndBuilding(previousYear,
                DateUtil.getMonth(currentDate),
                buildingId);
        return bills;
    }

    //---------------------------------------------------Inner Classes --------------------------------------------------------------
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

            bills = billService.filterByFromDateAndMonthAndBuilding(previousYear,
                    month1,
                    month2,
                    month3,
                    buildingId);
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

    @Getter
    public static class PreviousYearsParamData {
        public List<Bill> bills;
        public Float consumptionSum;
        public Float costSum;
        public Float environmentSum;

        PreviousYearsParamData(List<Bill> bills) {
            this.bills = bills;
        }

        Integer getBillSize() {
            return this.bills.size();
        }

        PreviousYearsParamData invoke() {
            consumptionSum = 0f;
            costSum = 0f;
            environmentSum = 0f;
            for (Bill bill : bills) {
                consumptionSum += bill.getTotalMonthlyConsumption();
                costSum += bill.getTotalPayable();
                // environmentSum += bill.getProducedCO2();
                environmentSum += bill.getTotalMonthlyConsumption() * CO2_CONS;
            }
            return this;
        }
    }
}