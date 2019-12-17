package tech.builtrix.controllers.report;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.builtrix.Response;
import tech.builtrix.base.ControllerBase;
import tech.builtrix.dtos.bill.BuildingDto;
import tech.builtrix.dtos.bill.EnergyConsumptionIndex;
import tech.builtrix.dtos.report.*;
import tech.builtrix.enums.DatePartType;
import tech.builtrix.enums.TimePeriodType;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.models.user.User;
import tech.builtrix.services.building.BuildingService;
import tech.builtrix.services.report.ReportService;

import java.util.List;

/**
 * Created By sahar at 12/4/19
 */

@RestController
@RequestMapping("/v1/reports")
@Api(value = "Report Controller", tags = {"Report Controller"})
//TODO check if building id is null
public class ReportController extends ControllerBase {

    private final ReportService reportService;
    private final BuildingService buildingService;

    @Autowired
    public ReportController(ReportService reportService, BuildingService buildingService) {
        this.reportService = reportService;
        this.buildingService = buildingService;
    }

    @ApiOperation(value = "Request for getting prediction data")
    @GetMapping(value = "/prediction")
    public Response<PredictionDto> prediction() {
        PredictionDto predictionDto = null;
        predictionDto = this.reportService.predict(getBuildingId());
        return Response.ok(predictionDto);
    }

    @ApiOperation(value = "Request for getting saving this month data")
    @GetMapping(value = "/saving")
    public Response<SavingDto> savingThisMonth() throws NotFoundException {
        SavingDto savingDto = null;
        savingDto = this.reportService.savingThisMonth(getBuildingId());
        return Response.ok(savingDto);
    }

    @ApiOperation(value = "Request for getting be score")
    @GetMapping(value = "/beScore")
    public Response<Float> getBEScore() throws NotFoundException {
        Float beScore = null;
        beScore = this.reportService.getBEScore(getBuildingId());
        return Response.ok(beScore);
    }

    @ApiOperation(value = "Request for getting be score")
    @GetMapping(value = "/nationalMedian")
    public Response<Float> getNationalMedian() throws NotFoundException {
        Float nationalMedian = null;
        nationalMedian = this.reportService.getNationalMedian(getBuildingId());
        return Response.ok(nationalMedian);
    }

    @ApiOperation(value = "Request for getting be score")
    @GetMapping(value = "/propertyTarget")
    public Response<Float> getPropertyTarget() throws NotFoundException {
        Float propertyTarget = null;
        propertyTarget = this.reportService.getDefaultPropertyTarget(getBuildingId(), null);
        return Response.ok(propertyTarget);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/costStack")
    public Response<CostStackDto> getCostStackData() throws NotFoundException {
        CostStackDto costStackData = null;
        costStackData = this.reportService.getCostStackData(getBuildingId());
        return Response.ok(costStackData);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/costPie")
    public Response<CostPieDto> getCostPieData() throws NotFoundException {
        CostPieDto costPieDto = null;
        costPieDto = this.reportService.getCostPieData(getBuildingId());
        return Response.ok(costPieDto);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/consumption")
    public Response<ConsumptionDto> getConsumption() throws NotFoundException {
        ConsumptionDto consumption = null;
        consumption = this.reportService.getConsumption(getBuildingId());
        return Response.ok(consumption);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/consumptionDynamic")
    public Response<ConsumptionDynamicDto> getConsumptionDynamicData(
            @RequestParam(value = "year") int year,
            @RequestParam(value = "periodType") TimePeriodType periodType,
            @RequestParam(value = "datePartType") DatePartType datePartType) throws NotFoundException {
        ConsumptionDynamicDto consumption = null;
        consumption = this.reportService.getConsumptionDynamicData(getBuildingId(), year, periodType, datePartType);
        return Response.ok(consumption);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/normConsumptionWeather")
    public Response<ConsumptionNormalWeatherDto> getConsumptionNormalWeather() throws NotFoundException {
        ConsumptionNormalWeatherDto consumption = null;
        consumption = this.reportService.getConsumptionNormalWeather(getBuildingId());
        return Response.ok(consumption);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/normPerCapita")
    public Response<NormalPerCapitaDto> getNormalizedPerCapita() throws NotFoundException {
        NormalPerCapitaDto normalizedPerCapita = null;
        normalizedPerCapita = this.reportService.getNormalizedPerCapita(getBuildingId());
        return Response.ok(normalizedPerCapita);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/normVSEE")
    public Response<NormalVsEEDto> getNormalizedVsEnergyEfficiency() {
        NormalVsEEDto dto = null;
        dto = this.reportService.getNormalizedVsEnergyEfficiency(getBuildingId());
        return Response.ok(dto);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/predictedWeatherVSReal")
    public Response<PredictedWeatherVsRealDto> getPredictedWeatherVSReal() throws NotFoundException {
        PredictedWeatherVsRealDto dto = null;
        dto = this.reportService.getPredictedWeatherVSReal(getBuildingId());
        return Response.ok(dto);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/carbonPie")
    public Response<CarbonPieDto> getCarbonPieData() throws NotFoundException {
        CarbonPieDto dto = null;
        dto = this.reportService.getCarbonPieData(getBuildingId());
        return Response.ok(dto);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/carbonSPLine")
    public Response<CarbonSPLineDto> getCarbonSPLineData() throws NotFoundException {
        CarbonSPLineDto dto = null;
        dto = this.reportService.getCarbonSPLineData(getBuildingId());
        return Response.ok(dto);
    }

    @ApiOperation(value = "Request for ")
    @GetMapping(value = "/energyConsumptionIndex")
    public Response<EnergyConsumptionIndexDto> getEnergyConsumptionIndex() throws NotFoundException {
        List<EnergyConsumptionIndex> indexes;
        EnergyConsumptionIndexDto dto = null;
        indexes = this.reportService.getAllEnergyConsumptionIndexes(getBuildingId());
        dto = new EnergyConsumptionIndexDto(indexes.get(0),
                indexes.get(1),
                indexes.get(2),
                indexes.get(3));
        return Response.ok(dto);
    }

    private String getBuildingId() {
        User user = this.requestContext.getUser();
        BuildingDto building = this.buildingService.findByOwner(user.getId());
        if (building != null) {
            return building.getId();
        }
        return null;
    }
}