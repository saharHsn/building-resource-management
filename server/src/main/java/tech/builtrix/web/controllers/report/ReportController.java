package tech.builtrix.web.controllers.report;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.builtrix.Response;
import tech.builtrix.base.ControllerBase;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.services.bill.BillService;
import tech.builtrix.services.building.BuildingService;
import tech.builtrix.services.historical.HistoricalConsumptionService;
import tech.builtrix.services.historical.HourlyDailyService;
import tech.builtrix.services.report.DataType;
import tech.builtrix.services.report.ReportService;
import tech.builtrix.web.dtos.bill.BillDto;
import tech.builtrix.web.dtos.bill.ReportIndex;
import tech.builtrix.web.dtos.report.*;
import tech.builtrix.web.dtos.report.enums.DatePartType;
import tech.builtrix.web.dtos.report.enums.TimePeriodType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created By sahar at 12/4/19
 */

@RestController
@RequestMapping("/v1/reports")
@Api(value = "Report Controller", tags = {"Report Controller"})
@Slf4j
//TODO check if building id is null
public class ReportController extends ControllerBase {

    private final ReportService reportService;
    private final BuildingService buildingService;
    private final BillService billService;
    private final HistoricalConsumptionService historicalConsumptionService;
    private final HourlyDailyService hourlyDailyService;

    @Autowired
    public ReportController(ReportService reportService, BuildingService buildingService,
                            BillService billService,
                            HistoricalConsumptionService historicalConsumptionService, HourlyDailyService hourlyDailyService) {
        this.reportService = reportService;
        this.buildingService = buildingService;
        this.billService = billService;
        this.historicalConsumptionService = historicalConsumptionService;
        this.hourlyDailyService = hourlyDailyService;
    }

    @ApiOperation(value = "Request for getting current Month Summary")
    @GetMapping(value = "/currentMonthSummary/{buildingId}")
    public Response<CurrentMonthSummaryDto> currentMonthSummary(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        CurrentMonthSummaryDto currentMonthSummaryDto;
        currentMonthSummaryDto = this.reportService.currentMonthSummary(buildingId);
        return Response.ok(currentMonthSummaryDto);
    }


    @ApiOperation(value = "Request for getting prediction data")
    @GetMapping(value = "/prediction/{buildingId}")
    public Response<PredictionDto> prediction(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        PredictionDto predictionDto;
        predictionDto = this.reportService.predict(buildingId);
        return Response.ok(predictionDto);
    }

    @ApiOperation(value = "Request for getting saving this month data")
    @GetMapping(value = "/saving/{buildingId}")
    public Response<SavingDto> savingThisMonth(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        SavingDto savingDto;
        savingDto = this.reportService.savingThisMonth(buildingId);
        return Response.ok(savingDto);
    }

    @ApiOperation(value = "Request for getting be score")
    @GetMapping(value = "/beScore/{buildingId}")
    public Response<Float> getBEScore(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        Float beScore;
        long l = System.currentTimeMillis();
        List<BillDto> billDtos = this.billService.getBillsOfLast12Months(buildingId);
        logger.info("After filling missed bills : " + (System.currentTimeMillis() - l) / 1000);
        beScore = this.reportService.getBEScore(buildingId, billDtos);
        return Response.ok(beScore);
    }

    @ApiOperation(value = "Request for getting nationalMedian")
    @GetMapping(value = "/nationalMedian/{buildingId}")
    public Response<Float> getNationalMedian(@PathVariable("buildingId") String buildingId) {
        Float nationalMedian;
        nationalMedian = this.reportService.getNationalMedian();
        return Response.ok(nationalMedian);
    }

    @ApiOperation(value = "Request for getting property Target")
    @GetMapping(value = "/propertyTarget/{buildingId}")
    public Response<Float> getPropertyTarget(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        Float propertyTarget;
        propertyTarget = this.reportService.getDefaultPropertyTarget(buildingId, null);
        return Response.ok(propertyTarget);
    }

    @ApiOperation(value = "Request for costStack")
    @GetMapping(value = "/costStack/{buildingId}")
    public Response<CostStackDto> getCostStackData(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        CostStackDto costStackData;
        costStackData = this.reportService.getCostStackData(buildingId);
        return Response.ok(costStackData);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/costPie/{buildingId}")
    public Response<CostPieDto> getCostPieData(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        CostPieDto costPieDto;
        costPieDto = this.reportService.getCostPieData(buildingId);
        return Response.ok(costPieDto);
    }

    @ApiOperation(value = "Request for getConsumption")
    @GetMapping(value = "/consumption/{buildingId}")
    public Response<ConsumptionDto> getConsumption(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        ConsumptionDto consumption;
        consumption = this.reportService.getConsumption(buildingId);
        return Response.ok(consumption);
    }

    @ApiOperation(value = "Request for consumptionDynamic")
    @GetMapping(value = "/consumptionDynamic/{buildingId}")
    public Response<ConsumptionDynamicDto> getConsumptionDynamicData(@PathVariable("buildingId") String buildingId,
                                                                     @RequestParam(value = "year") int year,
                                                                     @RequestParam(value = "periodType") TimePeriodType periodType,
                                                                     @RequestParam(value = "datePartType") DatePartType datePartType) throws NotFoundException {
        ConsumptionDynamicDto consumption;
        consumption = this.reportService.getConsumptionDynamicData(buildingId, year, periodType, datePartType);
        return Response.ok(consumption);
    }

    @ApiOperation(value = "Request for normConsumptionWeather")
    @GetMapping(value = "/normConsumptionWeather/{buildingId}")
    public Response<ConsumptionNormalWeatherDto> getConsumptionNormalWeather(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        ConsumptionNormalWeatherDto consumption;
        consumption = this.reportService.getConsumptionNormalWeather(buildingId);
        return Response.ok(consumption);
    }

    @ApiOperation(value = "Request for normPerCapita")
    @GetMapping(value = "/normPerCapita/{buildingId}")
    public Response<NormalPerCapitaDto> getNormalizedPerCapita(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        NormalPerCapitaDto normalizedPerCapita;
        normalizedPerCapita = this.reportService.getNormalizedPerCapita(buildingId);
        return Response.ok(normalizedPerCapita);
    }

    @ApiOperation(value = "Request for normVSEE")
    @GetMapping(value = "/normVSEE/{buildingId}")
    public Response<NormalVsEEDto> getNormalizedVsEnergyEfficiency(@PathVariable("buildingId") String buildingId) {
        NormalVsEEDto dto;
        dto = this.reportService.getNormalizedVsEnergyEfficiency();
        return Response.ok(dto);
    }

    @ApiOperation(value = "Request for predictedWeatherVSReal")
    @GetMapping(value = "/predictedWeatherVSReal/{buildingId}")
    public Response<PredictedWeatherVsRealDto> getPredictedWeatherVSReal(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        PredictedWeatherVsRealDto dto;
        dto = this.reportService.getPredictedWeatherVSReal(buildingId);
        return Response.ok(dto);
    }

    @ApiOperation(value = "Request for carbonPie")
    @GetMapping(value = "/carbonPie/{buildingId}")
    public Response<CarbonPieDto> getCarbonPieData(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        CarbonPieDto dto;
        dto = this.reportService.getCarbonPieData(buildingId);
        return Response.ok(dto);
    }

    @ApiOperation(value = "Request for getCarbonSPLineData")
    @GetMapping(value = "/carbonSPLine/{buildingId}")
    public Response<CarbonSPLineDto> getCarbonSPLineData(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        CarbonSPLineDto dto;
        dto = this.reportService.getCarbonSPLineData(buildingId);
        return Response.ok(dto);
    }

    @ApiOperation(value = "Request for energyConsumptionIndex")
    @GetMapping(value = "/energyConsumptionIndex/{buildingId}")
    public Response<EnergyConsumptionIndexDto> getEnergyConsumptionIndex(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        List<ReportIndex> indexes;
        EnergyConsumptionIndexDto dto;
        long l = System.currentTimeMillis();
        indexes = this.reportService.getAllEnergyConsumptionIndexes(buildingId);
        logger.info("After getAllEnergyConsumptionIndexes : " + (System.currentTimeMillis() - l) / 1000);
        dto = new EnergyConsumptionIndexDto(indexes.get(0), indexes.get(1), indexes.get(2), indexes.get(3), indexes.get(4));
        return Response.ok(dto);
    }

    @ApiOperation(value = "Request for excelCustomersReport")
    @GetMapping(value = "/download/{buildingId}")
    public ResponseEntity<InputStreamResource> excelCustomersReport(@PathVariable("buildingId") String buildingId) throws IOException, NotFoundException {

        ByteArrayInputStream in = this.reportService.getDashboardReportUrl(buildingId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + "Building" + ".xlsx");
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    @CrossOrigin(origins = "*")
    @ApiOperation(value = "Request for getHistoricalConsumption")
    @GetMapping(value = "/historicalConsumption/{buildingId}")
    public Response<HistoricalConsumptionDto> getHistoricalConsumption(@PathVariable("buildingId") String buildingId,
                                                                       @RequestParam(value = "year") int year,
                                                                       @RequestParam(value = "month") int month) {
        logger.info("starting HistoricalConsumption");
        long l = System.currentTimeMillis();
        HistoricalConsumptionDto dto = this.historicalConsumptionService.getHistoricalConsumption(buildingId, year, month, DataType.CONSUMPTION);
        logger.info("HistoricalConsumption finished in :" + (System.currentTimeMillis() - l));
        return Response.ok(dto);
    }

    @ApiOperation(value = "Request for getHistoricalCost")
    @GetMapping(value = "/historicalCost/{buildingId}")
    public Response<HistoricalConsumptionDto> getHistoricalCost(@PathVariable("buildingId") String buildingId,
                                                                @RequestParam(value = "year") int year,
                                                                @RequestParam(value = "month") int month) {
        HistoricalConsumptionDto dto = this.historicalConsumptionService.getHistoricalConsumption(buildingId, year, month, DataType.COST);
        return Response.ok(dto);
    }

    /*  @ApiOperation(value = "Request for ")
      @GetMapping(value = "/persistHistorical")
      @NoSession
      public Response<Void> persistHistoricalData(@RequestParam(value = "buildingId") String buildingId) throws IOException {
          //make date from first day of month and another for last day of month
          this.hourlyDailyService.parseExcelData(buildingId);
          return Response.ok();
      }
  */
    /*private String getBuildingId() {
        User user = this.requestContext.getUser();
        BuildingDto building = this.buildingService.findByOwner(user.getId());
        if (building != null) {
            return building.getId();
        }
        return null;
    }*/
}
