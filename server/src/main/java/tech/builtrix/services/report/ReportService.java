package tech.builtrix.services.report;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.builtrix.controllers.report.ConsumptionNormalWeatherDto;
import tech.builtrix.controllers.report.PredictedWeatherVsRealDto;
import tech.builtrix.dtos.bill.BillDto;
import tech.builtrix.dtos.bill.BuildingDto;
import tech.builtrix.dtos.report.*;
import tech.builtrix.enums.DatePartType;
import tech.builtrix.enums.TimePeriodType;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.models.bill.Bill;
import tech.builtrix.services.bill.BillService;
import tech.builtrix.services.building.BuildingService;
import tech.builtrix.utils.DateUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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

    private static Float kg_CO2_per_each_kWh = 0.408f;

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
        dto.setSavingYValues(getSavings(month1Cost, month2Cost, month3Cost, billsSize));
        //dto.setXValues(Arrays.asList("Oct-2019", "Nov-2019", "Dec-2019"));

        int currentYear = getCurrentYear();
        dto.setXValues(Arrays.asList(
                getDateTitle(month1, currentYear),
                getDateTitle(month2, currentYear),
                getDateTitle(month3, currentYear)));
        return dto;
    }

    public SavingDto savingThisMonth(String buildingId) throws NotFoundException {
        Date currentDate = getCurrentDate();
        Date previousYear = DateUtil.increaseDate(currentDate, -1, DateUtil.DateType.YEAR);
        List<Bill> bills = billService.filterByFromDateAndMonthAndBuilding(previousYear,
                DateUtil.getMonth(currentDate),
                buildingId);
        Bill lastBill = billService.getLastBill(buildingId);
        Float consumptionSum = 0f;
        Float costSum = 0f;
        Float environmentSum = 0f;
        for (Bill bill : bills) {
            //TODO fix this field
            consumptionSum += bill.getAverageDailyConsumption();
            costSum += bill.getTotalPayable();
            environmentSum += bill.getProducedCO2();
        }
        SavingDto dto = new SavingDto();
        dto.setConsumption((consumptionSum / bills.size()) - lastBill.getAverageDailyConsumption());
        dto.setCost((costSum / bills.size()) - lastBill.getTotalPayable());
        dto.setEnvironmental((environmentSum / bills.size()) - lastBill.getProducedCO2());
        /*dto.setConsumption(1263f);
        dto.setCost(190f);
        dto.setEnvironmental(515f);*/
        return dto;
    }

    public Float getBEScore(String buildingId) {
        Float reference = 50f;
        Float GOD = 0.1f * reference;
        //TODO what is x?
        Float x = billService.geXValue(buildingId);
        Float beScore = (GOD / x) * 100;
        //return 40f;
        return beScore;
    }

    public CostStackDto getCostStackData(String buildingId) throws NotFoundException {
        Integer year = getCurrentYear();
        List<BillDto> dtoList = getBillDtos(buildingId, year);

        CostStackDto dto = getCostStackDto(dtoList);

       /* dto.setContractedPowerValues(Arrays.asList(833.69f, 846.81f, 739.59f, 896.43f, 684.86f, 672.34f, 742.91f, 782.83f, 753.81f, 762.87f, 714.73f, 786.84f));
        dto.setFreeValues(Arrays.asList(437.62f, 402.36f, 467.34f, 460.2f, 369.59f, 414.66f, 368.28f, 406.59f, 453.63f, 397.01f, 427.34f, 556.36f));
        dto.setOffValues(Arrays.asList(265.96f, 282.73f, 238.51f, 250.71f, 211.15f, 209.45f, 209f, 228.47f, 227.03f, 220.12f, 245.33f, 310.36f));
        dto.setPeakValues(Arrays.asList(878.11f, 944.15f, 829.95f, 690.54f, 473.42f, 429.33f, 513.55f, 547.49f, 481.35f, 527.35f, 695.46f, 865.32f));
        dto.setPowerInPeakValues(Arrays.asList(833.69f, 846.81f, 739.59f, 896.43f, 684.86f, 672.34f, 742.91f, 782.83f, 753.81f, 762.87f, 714.73f, 786.84f));
        dto.setNormalValues(Arrays.asList(1844.74f, 1932.34f, 1715.54f, 1833.96f, 1538.25f, 1451.76f, 1729.1f, 878.35f, 1743.17f, 1881.22f, 1728.28f, 1834.11f));
        dto.setReactivePowerValues(Arrays.asList(9.34f, 9.32f, 10.21f, 10.56f, 13.36f, 16.35f, 9.78f, 6.43f, 6.4f, 9.37f, 12.25f, 6.02f));*/
        return dto;
    }


    public CostPieDto getCostPieData(String buildingId) throws NotFoundException {
        Date currentDate = getCurrentDate();
        int year = DateUtil.getYear(currentDate);
        List<BillDto> dtoList = new ArrayList<>();
        float contractedPower = 0f;
        float freeHours = 0f;
        float normalHours = 0f;
        float offHours = 0f;
        float powerInPeakHours = 0f;
        float reactivePower = 0f;
        float peakHours = 0f;
        for (int i = 0; i < 12; i++) {
            BillDto billDto = billService.filterByMonthAndYear(buildingId, i, year);
            dtoList.add(billDto);
        }
        for (BillDto billDto : dtoList) {
            contractedPower += billDto.getRDContractedPower().getCost();
            freeHours += billDto.getAEFreeHours().getCost();
            normalHours += billDto.getAENormalHours().getCost();
            offHours += billDto.getAENormalHours().getCost();
            powerInPeakHours = billDto.getAEPeakHours().getCost();
            reactivePower += billDto.getRDReactivePower().getCost();
            peakHours += billDto.getAEPeakHours().getCost();
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
        List<BillDto> dtoList = getBillDtos(buildingId, getCurrentYear());

        ConsumptionDto dto = getConsumptionDto(dtoList);

        /*dto.setContractedPowerValues(Arrays.asList(125.79f, 131.4f, 118.36f, 131.4f, 126.81f, 131.04f, 126.81f, 131.04f, 131.04f, 131.99f, 136.69f, 131.99f));
        dto.setPowerInPeakValues(Arrays.asList(833.69f, 846.81f, 739.59f, 896.43f, 684.86f, 672.34f, 742.91f, 782.83f, 753.81f, 762.87f, 714.73f, 786.84f));
        dto.setReactivePowerValues(Arrays.asList(9.34f, 9.32f, 10.21f, 10.56f, 13.36f, 16.35f, 9.78f, 6.43f, 6.4f, 9.37f, 12.25f, 6.02f));
        dto.setNormalValues(Arrays.asList(1844.74f, 1932.34f, 1715.54f, 1833.96f, 1538.25f, 1451.76f, 1729.1f, 878.35f, 1743.17f, 1881.22f, 1728.28f, 1834.11f));
        dto.setPeakValues(Arrays.asList(878.11f, 944.15f, 829.95f, 690.54f, 473.42f, 429.33f, 513.55f, 547.49f, 481.35f, 527.35f, 695.46f, 865.32f));
        dto.setFreeValues(Arrays.asList(437.62f, 402.36f, 467.34f, 460.2f, 369.59f, 414.66f, 368.28f, 406.59f, 453.63f, 397.01f, 427.34f, 556.36f));
        dto.setOffValues(Arrays.asList(265.96f, 282.73f, 238.51f, 250.71f, 211.15f, 209.45f, 209f, 228.47f, 227.03f, 220.12f, 245.33f, 310.36f));*/
        //TODO refine this data later
        dto.setBaseLineValues(Arrays.asList(1844.74f, 1932.34f, 1715.54f, 1833.96f, 1538.25f, 1451.76f, 1729.1f, 878.35f, 1743.17f, 1881.22f, 1728.28f, 1834.11f));
        return dto;
    }


    public ConsumptionDynamicDto getConsumptionDynamicData(String buildingId,
                                                           Integer year,
                                                           TimePeriodType periodType,
                                                           DatePartType datePartType) throws NotFoundException {
        List<BillDto> dtoList = getBillDtos(buildingId, year);
        ConsumptionDto monthlyConsDto = getConsumptionDto(dtoList);
        //TODO calculate quarter later
        boolean isQuarter = periodType.equals(TimePeriodType.QUARTERS);
        switch (datePartType) {
            case FREE_HOURS:
                return !isQuarter ? getConsumptionDynamicDto(FREEHOURS_COLOR, FREE_HOURS, monthlyConsDto.getFreeValues()) : getConsumptionDynamicDto(FREEHOURS_COLOR, FREE_HOURS, 9.34f, 9.32f, 10.21f, 10.56f);
            case NORMAL_HOURS:
                return !isQuarter ? getConsumptionDynamicDto(NORMALHOURS_COLOR, NORMAL_HOUR, monthlyConsDto.getNormalValues()) : getConsumptionDynamicDto(NORMALHOURS_COLOR, NORMAL_HOUR, 125.79f, 131.4f, 118.36f, 131.4f);
            case OFF_HOURS:
                return !isQuarter ? getConsumptionDynamicDto(OFFHOURS_COLOR, OFF_HOURS, monthlyConsDto.getOffValues()) : getConsumptionDynamicDto(OFFHOURS_COLOR, OFF_HOURS, 265.96f, 282.73f, 238.51f, 250.71f);
            case PEAK_HOURS:
                return !isQuarter ? getConsumptionDynamicDto(PEAKHOURS_COLOR, PEAK_HOURS, monthlyConsDto.getPeakValues()) : getConsumptionDynamicDto(PEAKHOURS_COLOR, PEAK_HOURS, 878.11f, 944.15f, 829.95f, 690.54f);
            default:
                throw new IllegalStateException("Unexpected value: " + datePartType);
        }
    }


    //TODO
    public ConsumptionNormalWeatherDto getConsumptionNormalWeather(String buildingId) {
        ConsumptionNormalWeatherDto dto = new ConsumptionNormalWeatherDto();
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
        dto.setStandardAValues(Arrays.asList(0f, 0f, 0f, 0.042f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f));
        dto.setStandardBValues(Arrays.asList(0f, 0f, 0f, 0f, 0.07f, 0.092f, 0f, 0.08f, 0.067f, 0f, 0f, 0f));
        dto.setStandardCValues(Arrays.asList(0.04f, 0.04f, 0.045f, 0f, 0f, 0f, 0.14f, 0f, 0f, 0.092f, 0.066f, 0.05f));
        dto.setStandardDValues(Arrays.asList(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f));
        return dto;
    }

    public NormalPerCapitaDto getNormalizedPerCapita(String buildingId) throws NotFoundException {
        NormalPerCapitaDto dto = new NormalPerCapitaDto();
        Integer currentYear = getCurrentYear();
        List<BillDto> dtoList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            dtoList.add(billService.filterByMonthAndYear(buildingId, i, currentYear));
        }
        List<Float> consumptionPerMonth = new ArrayList<>();
        BuildingDto building = this.buildingService.findById(buildingId);
        for (int i = 0; i < 12; i++) {
            Date date = getCustomDate(currentYear, i);
            Integer numOfPeople = building.getNumOfPeopleMap().get(date);
            consumptionPerMonth.add(i, (dtoList.get(i).getAverageDailyConsumption()) / numOfPeople);
        }
        //TODO ask for this data
        dto.setXValues(Arrays.asList("Jan-2018", "Feb-2018", "Mar-2018", "Apr-2018", "May-2018", "Jun-2018", "Jul-2018", "Aug-2018", "Sept-2018", "Oct-2018", "Nov-2018", "Dec-2018"));
        dto.setBaseLine(Arrays.asList(98.13f, 98.13f, 98.13f, 98.13f, 98.13f, 98.13f, 98.13f, 98.13f, 98.13f, 98.13f, 98.13f, 98.13f));
        dto.setTotal(consumptionPerMonth);
        return dto;
    }

    //TODO not needed now
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
        int currentYear = getCurrentYear();
        dto.setXValues(Arrays.asList(
                getDateTitle(month1, currentYear),
                getDateTitle(month2, currentYear),
                getDateTitle(month3, currentYear)));
        dto.setBaseLineValues(Arrays.asList(10.62f, 9.85f, 9.38f));
        // dto.setConsumptionValues(Arrays.asList(10.94f, 11.21f, 9.3f));
        return dto;
    }

    public CarbonPieDto getCarbonPieData(String buildingId) throws NotFoundException {
        BillDto lastBill = billService.getLastBillDto(buildingId);
        Float co2Free = (lastBill.getAEFreeHours().getConsumption() * kg_CO2_per_each_kWh) / lastBill.getProducedCO2();
        Float co2Normal = (lastBill.getAENormalHours().getConsumption() * kg_CO2_per_each_kWh) / lastBill.getProducedCO2();
        Float co2Off = (lastBill.getAEOffHours().getConsumption() * kg_CO2_per_each_kWh) / lastBill.getProducedCO2();
        Float co2Peak = (lastBill.getAEPeakHours().getConsumption() * kg_CO2_per_each_kWh) / lastBill.getProducedCO2();
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

    public CarbonSPLineDto getCarbonSPLineData(String buildingId) throws NotFoundException {
        Integer currentYear = getCurrentYear();
        List<BillDto> dtoList = getBillDtos(buildingId, currentYear);
        BuildingDto building = buildingService.findById(buildingId);
        List<Float> totalValues = new ArrayList<>();
        for (BillDto billDto : dtoList) {
            totalValues.add((billDto.getAverageDailyConsumption() * kg_CO2_per_each_kWh) / building.getArea());
        }
        // ConsumptionDto dto = getConsumptionDto(dtoList);
        CarbonSPLineDto dto = new CarbonSPLineDto();
        // dto.setTotalValues(Arrays.asList(4.28f, 4.51f, 3.95f, 4.07f, 3.27f, 3.2f, 3.53f, 3.83f, 3.69f, 3.78f, 3.88f, 4.52f));
        dto.setTotalValues(totalValues);
        Float baseLineValue = (100 * kg_CO2_per_each_kWh * building.getNumberOfPeople()) / building.getArea();
        // dto.setBaseLineValues(Arrays.asList(3.74f, 3.74f, 3.74f, 3.74f, 3.74f, 3.74f, 3.74f, 3.74f, 3.74f, 3.74f, 3.74f, 3.74f));
        List<Float> baseLineValues = IntStream.range(0, 12).mapToObj(i -> baseLineValue).collect(Collectors.toList());
        dto.setBaseLineValues(baseLineValues);
        List<String> xValues = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            xValues.add(getDateTitle(i, currentYear));
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

    //Report
    //BE Score
    //Breakdown
    //Percentile
    //Electricity consumption index (kWh/m2)
    //Electricity consumption index (kWh/cap)
    //Electricity cost index (Euro/m2)
    //Energy efficiency level


    //-------------------------------- private methods ------------------------------

    private String getDateTitle(Integer month, int year) {
        return DateUtil.getMonth(month) + "-" + year;
    }

    private List<Float> getSavings(Float month1Cost, Float month2Cost, Float month3Cost, int billsSize) {
        Float savings1 = 0.05f * (month1Cost / billsSize);
        Float savings2 = 0.05f * (month2Cost / billsSize);
        Float savings3 = 0.05f * month3Cost / billsSize;
        return Arrays.asList(savings1, savings2, savings3);
    }

    private Date getCurrentDate() {
        return new Date();
    }

    private CostStackDto getCostStackDto(List<BillDto> dtoList) {
        CostStackDto dto = new CostStackDto();
        List<Float> contractedPowerValues = new ArrayList<>();
        List<Float> powerInPeakValues = new ArrayList<>();
        List<Float> reactivePowerValues = new ArrayList<>();
        List<Float> normalValues = new ArrayList<>();
        List<Float> peakValues = new ArrayList<>();
        List<Float> freeValues = new ArrayList<>();
        List<Float> offValues = new ArrayList<>();
        extractCostValues(dtoList, contractedPowerValues,
                powerInPeakValues,
                reactivePowerValues,
                normalValues,
                peakValues,
                freeValues,
                offValues);
        dto.setContractedPowerValues(contractedPowerValues);
        dto.setFreeValues(freeValues);
        dto.setOffValues(offValues);
        dto.setPeakValues(peakValues);
        dto.setPowerInPeakValues(powerInPeakValues);
        dto.setNormalValues(normalValues);
        dto.setReactivePowerValues(reactivePowerValues);
        //TODO refine later
        dto.setXValues(Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"));
        return dto;
    }

    private void extractCostValues(List<BillDto> dtoList, List<Float> contractedPowerValues, List<Float> powerInPeakValues, List<Float> reactivePowerValues, List<Float> normalValues, List<Float> peakValues, List<Float> freeValues, List<Float> offValues) {
        for (BillDto billDto : dtoList) {
            contractedPowerValues.add(billDto.getRDContractedPower().getCost());
            freeValues.add(billDto.getAEFreeHours().getCost());
            offValues.add(billDto.getAEOffHours().getCost());
            peakValues.add(billDto.getAEPeakHours().getCost());
            powerInPeakValues.add(billDto.getRDPeakHours().getCost());
            normalValues.add(billDto.getAENormalHours().getCost());
            reactivePowerValues.add(billDto.getRDReactivePower().getCost());
        }
    }

    private List<BillDto> getBillDtos(String buildingId, Integer year) throws NotFoundException {
        List<BillDto> dtoList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            BillDto billDto = billService.filterByMonthAndYear(buildingId, i, year);
            dtoList.add(billDto);
        }
        return dtoList;
    }

    private ConsumptionDto getConsumptionDto(List<BillDto> dtoList) {
        ConsumptionDto dto = new ConsumptionDto();
        //TODO refine later
        dto.setXValues(Arrays.asList("Jan-2018", "Feb-2018", "Mar-2018", "Apr-2018", "May-2018", "Jun-2018",
                "Jul-2018", "Aug-2018", "Sept-2018", "Oct-2018", "Nov-2018", "Dec-2018"));

        List<Float> contractedPowerValues = new ArrayList<>();
        List<Float> powerInPeakValues = new ArrayList<>();
        List<Float> reactivePowerValues = new ArrayList<>();
        List<Float> normalValues = new ArrayList<>();
        List<Float> peakValues = new ArrayList<>();
        List<Float> freeValues = new ArrayList<>();
        List<Float> offValues = new ArrayList<>();
        extractConsumptionValues(dtoList,
                contractedPowerValues,
                powerInPeakValues,
                reactivePowerValues,
                normalValues,
                peakValues,
                freeValues,
                offValues);
        dto.setContractedPowerValues(contractedPowerValues);
        dto.setFreeValues(freeValues);
        dto.setOffValues(offValues);
        dto.setPeakValues(peakValues);
        dto.setPowerInPeakValues(powerInPeakValues);
        dto.setNormalValues(normalValues);
        dto.setReactivePowerValues(reactivePowerValues);
        return dto;
    }

    private void extractConsumptionValues(List<BillDto> dtoList,
                                          List<Float> contractedPowerValues,
                                          List<Float> powerInPeakValues,
                                          List<Float> reactivePowerValues,
                                          List<Float> normalValues,
                                          List<Float> peakValues,
                                          List<Float> freeValues,
                                          List<Float> offValues) {
        for (BillDto billDto : dtoList) {
            contractedPowerValues.add(billDto.getRDContractedPower().getConsumption());
            freeValues.add(billDto.getAEFreeHours().getConsumption());
            offValues.add(billDto.getAEOffHours().getConsumption());
            peakValues.add(billDto.getAEPeakHours().getConsumption());
            powerInPeakValues.add(billDto.getRDPeakHours().getConsumption());
            normalValues.add(billDto.getAENormalHours().getConsumption());
            reactivePowerValues.add(billDto.getRDReactivePower().getConsumption());
        }
    }

    //TODO reimplement later
    private ConsumptionDynamicDto getConsumptionDynamicDto(String s, String s2, float v, float v2, float v3, float v4) {
        ConsumptionDynamicDto freeHoursQ = new ConsumptionDynamicDto();
        freeHoursQ.setColor(s);
        freeHoursQ.setName(s2);
        freeHoursQ.setData(Arrays.asList(v, v2, v3, v4));
        return freeHoursQ;
    }

    private Integer getCurrentYear() {
        Date currentDate = getCurrentDate();
        return DateUtil.getYear(currentDate);
    }

    private ConsumptionDynamicDto getConsumptionDynamicDto(String color, String name, List<Float> data) {
        ConsumptionDynamicDto dto = new ConsumptionDynamicDto();
        dto.setColor(color);
        dto.setName(name);
        dto.setData(data);
        return dto;
    }

    private Date getCustomDate(Integer currentYear, int i) {
        Date date = new Date();
        DateUtil.setDateField(date, currentYear, DateUtil.DateType.YEAR);
        DateUtil.setDateField(date, i, DateUtil.DateType.MONTH);
        DateUtil.setDateField(date, 1, DateUtil.DateType.DAY);
        DateUtil.removeTime(date);
        return date;
    }

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

        public PredictionData(String buildingId) {
            this.buildingId = buildingId;
        }

        public Date getCurrentDate() {
            return currentDate;
        }

        public Integer getMonth1() {
            return month1;
        }

        public Integer getMonth2() {
            return month2;
        }

        public Integer getMonth3() {
            return month3;
        }

        public Float getMonth1Cost() {
            return month1Cost;
        }

        public Float getMonth2Cost() {
            return month2Cost;
        }

        public Float getMonth3Cost() {
            return month3Cost;
        }

        public Float getMonth1Consumption() {
            return month1Consumption;
        }

        public Float getMonth2Consumption() {
            return month2Consumption;
        }

        public Float getMonth3Consumption() {
            return month3Consumption;
        }

        public List<Bill> getBills() {
            return bills;
        }

        public PredictionData invoke() {
            currentDate = getCurrentDate();
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
                    month1Consumption += bill.getAverageDailyConsumption();
                } else if (fromMonth.equals(month2)) {
                    month2Cost += bill.getTotalPayable();
                    month2Consumption += bill.getAverageDailyConsumption();
                } else if (fromMonth.equals(month3)) {
                    month3Cost += bill.getTotalPayable();
                    month3Consumption += bill.getAverageDailyConsumption();
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
}