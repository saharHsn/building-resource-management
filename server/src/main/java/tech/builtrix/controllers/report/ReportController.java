package tech.builtrix.controllers.report;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.builtrix.Response;
import tech.builtrix.annotations.NoSession;
import tech.builtrix.base.ControllerBase;
import tech.builtrix.dtos.bill.EnergyConsumptionIndex;
import tech.builtrix.dtos.report.*;
import tech.builtrix.enums.DatePartType;
import tech.builtrix.enums.TimePeriodType;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.services.report.ReportService;

import java.util.List;

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
        PredictionDto predictionDto = null;
        try {
            predictionDto = this.reportService.predict(buildingId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok(predictionDto);
    }

    @ApiOperation(value = "Request for getting saving this month data")
    @GetMapping(value = "/saving/{buildingId}")
    @NoSession
    public Response<SavingDto> savingThisMonth(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        SavingDto savingDto = null;
        try {
            savingDto = this.reportService.savingThisMonth(buildingId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok(savingDto);
    }

    @ApiOperation(value = "Request for getting be score")
    @GetMapping(value = "/beScore/{buildingId}")
    @NoSession
    public Response<Float> getBEScore(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        Float beScore = null;
        try {
            beScore = this.reportService.getBEScore(buildingId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok(beScore);
    }

    @ApiOperation(value = "Request for getting be score")
    @GetMapping(value = "/nationalMedian/{buildingId}")
    @NoSession
    public Response<Float> getNationalMedian(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        Float nationalMedian = null;
        try {
            nationalMedian = this.reportService.getNationalMedian(buildingId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok(nationalMedian);
    }

    @ApiOperation(value = "Request for getting be score")
    @GetMapping(value = "/propertyTarget/{buildingId}")
    @NoSession
    public Response<Float> getPropertyTarget(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        Float propertyTarget = null;
        try {
            propertyTarget = this.reportService.getDefaultPropertyTarget(buildingId, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok(propertyTarget);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/costStack/{buildingId}")
    @NoSession
    public Response<CostStackDto> getCostStackData(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        CostStackDto costStackData = null;
        try {
            costStackData = this.reportService.getCostStackData(buildingId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok(costStackData);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/costPie/{buildingId}")
    @NoSession
    public Response<CostPieDto> getCostPieData(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        CostPieDto costPieDto = null;
        try {
            costPieDto = this.reportService.getCostPieData(buildingId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok(costPieDto);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/consumption/{buildingId}")
    @NoSession
    public Response<ConsumptionDto> getConsumption(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        ConsumptionDto consumption = null;
        try {
            consumption = this.reportService.getConsumption(buildingId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok(consumption);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/consumptionDynamic/{buildingId}")
    @NoSession
    public Response<ConsumptionDynamicDto> getConsumptionDynamicData(@PathVariable("buildingId") String buildingId,
                                                                     @RequestParam(value = "year") int year,
                                                                     @RequestParam(value = "periodType") TimePeriodType periodType,
                                                                     @RequestParam(value = "datePartType") DatePartType datePartType) throws NotFoundException {
        ConsumptionDynamicDto consumption = null;
        try {
            consumption = this.reportService.getConsumptionDynamicData(buildingId, year, periodType, datePartType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok(consumption);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/normConsumptionWeather/{buildingId}")
    @NoSession
    public Response<ConsumptionNormalWeatherDto> getConsumptionNormalWeather(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        ConsumptionNormalWeatherDto consumption = null;
        try {
            consumption = this.reportService.getConsumptionNormalWeather(buildingId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok(consumption);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/normPerCapita/{buildingId}")
    @NoSession
    public Response<NormalPerCapitaDto> getNormalizedPerCapita(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        NormalPerCapitaDto normalizedPerCapita = null;
        try {
            normalizedPerCapita = this.reportService.getNormalizedPerCapita(buildingId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok(normalizedPerCapita);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/normVSEE/{buildingId}")
    @NoSession
    public Response<NormalVsEEDto> getNormalizedVsEnergyEfficiency(@PathVariable("buildingId") String buildingId) {
        NormalVsEEDto dto = null;
        try {
            dto = this.reportService.getNormalizedVsEnergyEfficiency(buildingId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok(dto);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/predictedWeatherVSReal/{buildingId}")
    @NoSession
    public Response<PredictedWeatherVsRealDto> getPredictedWeatherVSReal(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        PredictedWeatherVsRealDto dto = null;
        try {
            dto = this.reportService.getPredictedWeatherVSReal(buildingId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok(dto);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/carbonPie/{buildingId}")
    @NoSession
    public Response<CarbonPieDto> getCarbonPieData(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        CarbonPieDto dto = null;
        try {
            dto = this.reportService.getCarbonPieData(buildingId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok(dto);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/carbonSPLine/{buildingId}")
    @NoSession
    public Response<CarbonSPLineDto> getCarbonSPLineData(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        CarbonSPLineDto dto = null;
        try {
            dto = this.reportService.getCarbonSPLineData(buildingId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok(dto);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/energyConsumptionIndex/{buildingId}")
    @NoSession
    public Response<EnergyConsumptionIndexDto> getEnergyConsumptionIndex(@PathVariable("buildingId") String buildingId) throws NotFoundException {
        List<EnergyConsumptionIndex> indexes;
        EnergyConsumptionIndexDto dto = null;
        try {
            indexes = this.reportService.getAllEnergyConsumptionIndexes(buildingId);
            dto = new EnergyConsumptionIndexDto(indexes.get(0),
                    indexes.get(1),
                    indexes.get(2),
                    indexes.get(3));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok(dto);
    }
}