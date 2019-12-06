package tech.builtrix.controller.report;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.builtrix.Response;
import tech.builtrix.base.ControllerBase;
import tech.builtrix.security.session.NoSession;
import tech.builtrix.service.report.ReportService;

/**
 * Created By sahar at 12/4/19
 */

@RestController
@RequestMapping("/v1/reports")
@Api(value = "Report Controller", tags = {"Report Controller"})
public class ReportController extends ControllerBase {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }


    @ApiOperation(value = "Request for getting prediction data")
    @GetMapping(value = "/prediction/{buildingId}")
    @NoSession
    public Response<PredictionDto> prediction(@PathVariable("buildingId") String buildingId) {
        PredictionDto predictionDto = this.reportService.predict(buildingId);
        return Response.ok(predictionDto);
    }

    @ApiOperation(value = "Request for getting saving this month data")
    @GetMapping(value = "/saving/{buildingId}")
    @NoSession
    public Response<SavingDto> savingThisMonth(@PathVariable("buildingId") String buildingId) {
        SavingDto savingDto = this.reportService.savingThisMonth(buildingId);
        return Response.ok(savingDto);
    }

    @ApiOperation(value = "Request for getting be score")
    @GetMapping(value = "/beScore/{buildingId}")
    @NoSession
    public Response<Float> getBEScore(@PathVariable("buildingId") String buildingId) {
        Float beScore = this.reportService.getBEScore(buildingId);
        return Response.ok(beScore);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/costStack/{buildingId}")
    @NoSession
    public Response<CostStackDto> getCostStackData(@PathVariable("buildingId") String buildingId) {
        CostStackDto costStackData = this.reportService.getCostStackData(buildingId);
        return Response.ok(costStackData);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/costPie/{buildingId}")
    @NoSession
    public Response<CostPieDto> getCostPieData(@PathVariable("buildingId") String buildingId) {
        CostPieDto costPieDto = this.reportService.getCostPieData(buildingId);
        return Response.ok(costPieDto);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/consumption/{buildingId}")
    @NoSession
    public Response<ConsumptionDto> getConsumption(@PathVariable("buildingId") String buildingId) {
        ConsumptionDto consumption = this.reportService.getConsumption(buildingId);
        return Response.ok(consumption);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/consumptionDynamic/{buildingId}")
    @NoSession
    public Response<ConsumptionDynamicDto> getConsumptionDynamicData(@PathVariable("buildingId") String buildingId,
                                                                     @RequestParam(value = "year") Long year,
                                                                     @RequestParam(value = "periodType") TimePeriodType periodType,
                                                                     @RequestParam(value = "datePartType") DatePartType datePartType) {
        ConsumptionDynamicDto consumption = this.reportService.getConsumptionDynamicData(buildingId, year, periodType, datePartType);
        return Response.ok(consumption);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/normConsumptionWeather/{buildingId}")
    @NoSession
    public Response<ConsumptionNormalWeatherDto> getConsumptionNormalWeather(@PathVariable("buildingId") String buildingId) {
        ConsumptionNormalWeatherDto consumption = this.reportService.getConsumptionNormalWeather(buildingId);
        return Response.ok(consumption);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/normPerCapita/{buildingId}")
    @NoSession
    public Response<NormalPerCapitaDto> getNormalizedPerCapita(@PathVariable("buildingId") String buildingId) {
        NormalPerCapitaDto normalizedPerCapita = this.reportService.getNormalizedPerCapita(buildingId);
        return Response.ok(normalizedPerCapita);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/normPerCapita/{buildingId}")
    @NoSession
    public Response<NormalVsEEDto> getNormalizedVsEnergyEfficiency(@PathVariable("buildingId") String buildingId) {
        NormalVsEEDto dto = this.reportService.getNormalizedVsEnergyEfficiency(buildingId);
        return Response.ok(dto);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/predictedWeatherVSReal/{buildingId}")
    @NoSession
    public Response<PredictedWeatherVsRealDto> getPredictedWeatherVSReal(@PathVariable("buildingId") String buildingId) {
        PredictedWeatherVsRealDto dto = this.reportService.getPredictedWeatherVSReal(buildingId);
        return Response.ok(dto);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/carbonPie/{buildingId}")
    @NoSession
    public Response<CarbonPieDto> getCarbonPieData(@PathVariable("buildingId") String buildingId) {
        CarbonPieDto dto = this.reportService.getCarbonPieData(buildingId);
        return Response.ok(dto);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/carbonSPLine/{buildingId}")
    @NoSession
    public Response<CarbonSPLineDto> getCarbonSPLineData(@PathVariable("buildingId") String buildingId) {
        CarbonSPLineDto dto = this.reportService.getCarbonSPLineData(buildingId);
        return Response.ok(dto);
    }
}