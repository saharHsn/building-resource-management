package tech.builtrix;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tech.builtrix.enums.DatePartType;
import tech.builtrix.enums.TimePeriodType;
import tech.builtrix.exceptions.NotFoundException;
import tech.builtrix.services.bill.BillService;
import tech.builtrix.services.report.ReportService;
import tech.builtrix.web.dtos.bill.BillDto;
import tech.builtrix.web.dtos.bill.ReportIndex;
import tech.builtrix.web.dtos.report.*;

import java.io.IOException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class ApplicationTests {
	@Autowired
	private BillService billService;
	@Autowired
	private ReportService reportService;
	private static String BUILDING_ID = "9d94dd4d-b789-4717-bdee-517a8de8ca6e";
	private Integer year = 2020;
	private TimePeriodType periodType = TimePeriodType.MONTHLY;
	private DatePartType datePartType = DatePartType.FREE_HOURS;

	@Test
	public void contextLoads() throws NotFoundException {
		prediction();
		savings();

	}

	private void savings() throws NotFoundException {
        SavingDto savingDto = null;
        savingDto = this.reportService.savingThisMonth(BUILDING_ID);
    }

    private void prediction() {
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
		dto = new EnergyConsumptionIndexDto(indexes.get(0), indexes.get(1), indexes.get(2), indexes.get(3));
		return dto;
	}

	@Test
	public void downloadDashboard() throws NotFoundException, IOException {
		this.reportService.getDashboardReportUrl(BUILDING_ID);
	}
}
