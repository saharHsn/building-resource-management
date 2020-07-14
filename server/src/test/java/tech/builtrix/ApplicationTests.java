package tech.builtrix;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tech.builtrix.configurations.DynamoDBConfig;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.models.user.enums.Role;
import tech.builtrix.services.authenticate.CodeService;
import tech.builtrix.services.bill.BillService;
import tech.builtrix.services.historical.HistoricalConsumptionService;
import tech.builtrix.services.historical.HourlyDailyService;
import tech.builtrix.services.report.DataType;
import tech.builtrix.services.report.ReportService;
import tech.builtrix.services.user.UserService;
import tech.builtrix.utils.DateUtil;
import tech.builtrix.web.dtos.bill.BillDto;
import tech.builtrix.web.dtos.bill.ReportIndex;
import tech.builtrix.web.dtos.historical.HistoricalEnergyConsumptionDto;
import tech.builtrix.web.dtos.report.*;
import tech.builtrix.web.dtos.report.enums.DatePartType;
import tech.builtrix.web.dtos.report.enums.TimePeriodType;
import tech.builtrix.web.dtos.user.UserDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class ApplicationTests {
    @Autowired
    private BillService billService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private HourlyDailyService hourlyDailyService;
    @Autowired
    private DynamoDBConfig dynamoDBConfig;
    @Autowired
    private CodeService codeService;

    @Autowired
    private UserService userService;

    @Autowired
    private HistoricalConsumptionService historicalConsumptionService;

    //franklin
    //private static String BUILDING_ID = "9d94dd4d-b789-4717-bdee-517a8de8ca6e";
    //parede
    // private static String BUILDING_ID = "4f9e5bc1-471d-4b37-87ca-82f803898bb6";
    //demo
    private static String BUILDING_ID = "fae0c9a2-ef89-477a-a073-a9e704e5ccb3";
    private Integer year = 2020;
    private TimePeriodType periodType = TimePeriodType.MONTHLY;
    private DatePartType datePartType = DatePartType.FREE_HOURS;

    @Test
    public void contextLoads() throws NotFoundException {
        prediction();
        savings();

    }

    @Test
    public void createNewUser() throws NotFoundException {
        UserDto user = new UserDto();
        user.setEmailAddress("Super.User@cascaisambiente.pt");
        user.setFirstName("Super User");
        user.setLastName("Super User");
        user.setPassword("e34a622d0ad5aa1ae104c0839d60f8955093bf31");
        // user.setEnabled(true);
        user.setRole(Role.Senior);
        // user.setEmailConfirmed(true);
        this.userService.save(user);


        // return consumption;
    }

    private void savings() throws NotFoundException {
        SavingDto savingDto = null;
        savingDto = this.reportService.savingThisMonth(BUILDING_ID);
    }

    private void prediction() throws NotFoundException {
        // prediction
        PredictionDto predictionDto = null;
        predictionDto = this.reportService.predict(BUILDING_ID);
    }

    public Float getBEScore(String buildingId) throws NotFoundException {
        Float beScore;
        List<BillDto> billDtos = this.billService.getBillsOfLast12Months(buildingId);
        beScore = this.reportService.getBEScore(BUILDING_ID, billDtos);
        return beScore;
    }

    public Float getNationalMedian() throws NotFoundException {
        Float nationalMedian = null;
        nationalMedian = this.reportService.getNationalMedian();
        return nationalMedian;
    }

    public Float getPropertyTarget() throws NotFoundException {
        Float propertyTarget = null;
        propertyTarget = this.reportService.getDefaultPropertyTarget(BUILDING_ID, null);
        return propertyTarget;
    }

    public CostStackDto getCostStackData() throws NotFoundException {
        CostStackDto costStackData = null;
        costStackData = this.reportService.getCostStackData(BUILDING_ID);
        return costStackData;
    }

    public CostPieDto getCostPieData() throws NotFoundException {
        CostPieDto costPieDto = null;
        costPieDto = this.reportService.getCostPieData(BUILDING_ID);
        return costPieDto;
    }

    @Test
    public void getConsumption() throws NotFoundException {
        ConsumptionDto consumption = null;
        consumption = this.reportService.getConsumption(BUILDING_ID);
        System.out.println(consumption);
        // return consumption;
    }

    @Test
    public void getConsumptionDynamicData() throws NotFoundException {
        ConsumptionDynamicDto consumption = null;
        consumption = this.reportService.getConsumptionDynamicData(BUILDING_ID, year, periodType, datePartType);
        System.out.println(consumption);
        // return consumption;
    }

    public ConsumptionNormalWeatherDto getConsumptionNormalWeather() throws NotFoundException {
        ConsumptionNormalWeatherDto consumption = null;
        consumption = this.reportService.getConsumptionNormalWeather(BUILDING_ID);
        return consumption;
    }

    public NormalPerCapitaDto getNormalizedPerCapita() throws NotFoundException {
        NormalPerCapitaDto normalizedPerCapita = null;
        normalizedPerCapita = this.reportService.getNormalizedPerCapita(BUILDING_ID);
        return normalizedPerCapita;
    }

    public NormalVsEEDto getNormalizedVsEnergyEfficiency() {
        NormalVsEEDto dto = null;
        dto = this.reportService.getNormalizedVsEnergyEfficiency();
        return dto;
    }

    public PredictedWeatherVsRealDto getPredictedWeatherVSReal() throws NotFoundException {
        PredictedWeatherVsRealDto dto = null;
        dto = this.reportService.getPredictedWeatherVSReal(BUILDING_ID);
        return dto;
    }

    public CarbonPieDto getCarbonPieData() throws NotFoundException {
        CarbonPieDto dto = null;
        dto = this.reportService.getCarbonPieData(BUILDING_ID);
        return dto;
    }

    public CarbonSPLineDto getCarbonSPLineData() throws NotFoundException {
        CarbonSPLineDto dto = null;
        dto = this.reportService.getCarbonSPLineData(BUILDING_ID);
        return dto;
    }

    public EnergyConsumptionIndexDto getEnergyConsumptionIndex() throws NotFoundException {
        List<ReportIndex> indexes;
        EnergyConsumptionIndexDto dto = null;
        indexes = this.reportService.getAllEnergyConsumptionIndexes(BUILDING_ID);
        dto = new EnergyConsumptionIndexDto(indexes.get(0), indexes.get(1), indexes.get(2), indexes.get(3), indexes.get(4));
        return dto;
    }

    @Test
    public void downloadDashboard() throws NotFoundException, IOException {
        this.reportService.getDashboardReportUrl(BUILDING_ID);
    }

    @Test
    public void downloadS3File() throws IOException {
        // this.codeService.generateRandomNumber(33);
        // System.out.println();
        this.hourlyDailyService.parseExcelData("", BUILDING_ID);
        // this.hourlyDailyService.copyDateData();
    }

    @Test
    public void solarPanel() throws NotFoundException {
        float saving = this.reportService.getPossibleSolarSaving(BUILDING_ID, 2019);
        System.out.println("saving: " + saving);
        float sonarReq = this.reportService.getSolarReq(BUILDING_ID, 2019);
        System.out.println("sonarReq: " + sonarReq);
    }

    @Test
    public void heatMapHourly() {
        HeatMapHourlyDto dto = this.historicalConsumptionService.getHeatMapHourlyConsumption(BUILDING_ID, year, 3, DataType.CONSUMPTION);
        int i = 0;
        for (Object[] dataMatrix : dto.getDataMatrix()) {
            if (i == 24) {
                i = 0;
                System.out.println("\n");
            } else {
                i++;
            }
            System.out.print(Arrays.toString(dataMatrix) + ",");
        }
    }

    @Test
    public void heatMapDaily() {
        HeatMapDailyDto dto = this.historicalConsumptionService.getHeatMapDailyConsumption(BUILDING_ID, year, DataType.CONSUMPTION);
        int i = 0;
        for (DailyMatrix dataMatrix : dto.getDataMatrix()) {
            if (i == 24) {
                i = 0;
                System.out.println("\n");
            } else {
                i++;
            }
            System.out.print(dataMatrix + ",");
        }
    }

    @Test
    public void removeHistoricalDuplicates() {
        removeDuplicates();
    }

    private List<HistoricalEnergyConsumptionDto> removeDuplicates() {
        String dateStr = year + "-" + "01" + "-" + "01T" + "00:00:00.000Z";
        String dateStr1 = year + "-" + "06" + "-" + DateUtil.getNumOfDaysOfMonth(year, 3) + "T23:59:59.000Z";
        List<HistoricalEnergyConsumptionDto> dtoList = this.historicalConsumptionService.filterByDate(BUILDING_ID, dateStr, dateStr1);
        long l = System.currentTimeMillis();

        List<HistoricalEnergyConsumptionDto> uniqueList = new ArrayList<>();
        dtoList.forEach(historicalEnergyConsumptionDto -> {
            if (!uniqueList.contains(historicalEnergyConsumptionDto)) {
                uniqueList.add(historicalEnergyConsumptionDto);
            } else {
                System.out.println("Find duplicate for date : " + DateUtil.removeTime(historicalEnergyConsumptionDto.getDate()) + " and hour : " + historicalEnergyConsumptionDto.getHour());
                this.historicalConsumptionService.delete(historicalEnergyConsumptionDto.getId());
            }
        });
        // logger.info("removing duplicates : " + (System.currentTimeMillis() - l));
        return uniqueList;
    }
}
