package tech.builtrix.services.report;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.builtrix.controllers.report.ConsumptionNormalWeatherDto;
import tech.builtrix.controllers.report.PredictedWeatherVsRealDto;
import tech.builtrix.dtos.report.*;
import tech.builtrix.enums.DatePartType;
import tech.builtrix.enums.TimePeriodType;

import java.util.Arrays;

/**
 * Created By sahar at 12/4/19
 */
@Component
@Slf4j
public class ReportService {

    public PredictionDto predict(String buildingId) {
        PredictionDto dto = new PredictionDto();
        dto.setCostYValues(Arrays.asList(6135.5f, 7130.4f, 6234.3f));
        dto.setSavingYValues(Arrays.asList(321f, 420f, 360f));
        dto.setXValues(Arrays.asList("Oct-2019", "Nov-2019", "Dec-2019"));
        return dto;
    }

    public SavingDto savingThisMonth(String buildingId) {
        SavingDto dto = new SavingDto();
        dto.setConsumption(1263f);
        dto.setCost(190f);
        dto.setEnvironmental(515f);
        return dto;
    }

    public Float getBEScore(String buildingId) {
        return 40f;
    }

    public CostStackDto getCostStackData(String buildingId) {
        CostStackDto dto = new CostStackDto();
        dto.setContractedPowerValues(Arrays.asList(833.69f, 846.81f, 739.59f, 896.43f, 684.86f, 672.34f, 742.91f, 782.83f, 753.81f, 762.87f, 714.73f, 786.84f));
        dto.setFreeValues(Arrays.asList(437.62f, 402.36f, 467.34f, 460.2f, 369.59f, 414.66f, 368.28f, 406.59f, 453.63f, 397.01f, 427.34f, 556.36f));
        dto.setOffValues(Arrays.asList(265.96f, 282.73f, 238.51f, 250.71f, 211.15f, 209.45f, 209f, 228.47f, 227.03f, 220.12f, 245.33f, 310.36f));
        dto.setPeakValues(Arrays.asList(878.11f, 944.15f, 829.95f, 690.54f, 473.42f, 429.33f, 513.55f, 547.49f, 481.35f, 527.35f, 695.46f, 865.32f));
        dto.setXValues(Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"));
        dto.setPowerInPeakValues(Arrays.asList(833.69f, 846.81f, 739.59f, 896.43f, 684.86f, 672.34f, 742.91f, 782.83f, 753.81f, 762.87f, 714.73f, 786.84f));
        dto.setNormalValues(Arrays.asList(1844.74f, 1932.34f, 1715.54f, 1833.96f, 1538.25f, 1451.76f, 1729.1f, 878.35f, 1743.17f, 1881.22f, 1728.28f, 1834.11f));
        dto.setReactivePowerValues(Arrays.asList(9.34f, 9.32f, 10.21f, 10.56f, 13.36f, 16.35f, 9.78f, 6.43f, 6.4f, 9.37f, 12.25f, 6.02f));
        return dto;
    }

    public CostPieDto getCostPieData(String buildingId) {
        CostPieDto dto = new CostPieDto();
        dto.setContractedPower(3.2f);
        dto.setFreeHours(10.8f);
        dto.setNormalHours(43.6f);
        dto.setOffHours(6.1f);
        dto.setPowerInPeakHours(18.08f);
        dto.setReactivePower(0.2f);
        dto.setPeakHours(17.3f);
        return dto;
    }

    public ConsumptionDto getConsumption(String buildingId) {
        ConsumptionDto dto = new ConsumptionDto();
        dto.setXValues(Arrays.asList("Jan-2018", "Feb-2018", "Mar-2018", "Apr-2018", "May-2018", "Jun-2018",
                "Jul-2018", "Aug-2018", "Sept-2018", "Oct-2018", "Nov-2018", "Dec-2018"));
        dto.setContractedPowerValues(Arrays.asList(125.79f, 131.4f, 118.36f, 131.4f, 126.81f, 131.04f, 126.81f, 131.04f, 131.04f, 131.99f, 136.69f, 131.99f));
        dto.setPowerInPeakValues(Arrays.asList(833.69f, 846.81f, 739.59f, 896.43f, 684.86f, 672.34f, 742.91f, 782.83f, 753.81f, 762.87f, 714.73f, 786.84f));
        dto.setReactivePowerValues(Arrays.asList(9.34f, 9.32f, 10.21f, 10.56f, 13.36f, 16.35f, 9.78f, 6.43f, 6.4f, 9.37f, 12.25f, 6.02f));
        dto.setNormalValues(Arrays.asList(1844.74f, 1932.34f, 1715.54f, 1833.96f, 1538.25f, 1451.76f, 1729.1f, 878.35f, 1743.17f, 1881.22f, 1728.28f, 1834.11f));
        dto.setPeakValues(Arrays.asList(878.11f, 944.15f, 829.95f, 690.54f, 473.42f, 429.33f, 513.55f, 547.49f, 481.35f, 527.35f, 695.46f, 865.32f));
        dto.setFreeValues(Arrays.asList(437.62f, 402.36f, 467.34f, 460.2f, 369.59f, 414.66f, 368.28f, 406.59f, 453.63f, 397.01f, 427.34f, 556.36f));
        dto.setOffValues(Arrays.asList(265.96f, 282.73f, 238.51f, 250.71f, 211.15f, 209.45f, 209f, 228.47f, 227.03f, 220.12f, 245.33f, 310.36f));
        dto.setBaseLineValues(Arrays.asList(1844.74f, 1932.34f, 1715.54f, 1833.96f, 1538.25f, 1451.76f, 1729.1f, 878.35f, 1743.17f, 1881.22f, 1728.28f, 1834.11f));
        return dto;
    }

    public ConsumptionDynamicDto getConsumptionDynamicData(String buildingId,
                                                           Long year,
                                                           TimePeriodType periodType, DatePartType datePartType) {
        ConsumptionDynamicDto peakHours = getConsumptionDynamicDto("#ff0000", "Peak Hours", 878.11f, 944.15f, 829.95f, 690.54f, 473.42f, 429.33f, 513.55f, 547.49f, 481.35f, 527.35f, 695.46f, 865.32f);
        ConsumptionDynamicDto peakHoursQ = getConsumptionDynamicDto("#ff0000", "Peak Hours", 878.11f, 944.15f, 829.95f, 690.54f);

        ConsumptionDynamicDto freeHours = getConsumptionDynamicDto("#ffff00", "Free Hours", 9.34f, 9.32f, 10.21f, 10.56f, 13.36f, 16.35f, 9.78f, 6.43f, 6.4f, 9.37f, 12.25f, 6.02f);

        ConsumptionDynamicDto freeHoursQ = getConsumptionDynamicDto("#ff0000", "Free Hours", 9.34f, 9.32f, 10.21f, 10.56f);

        ConsumptionDynamicDto normalHours = getConsumptionDynamicDto("#0066cc", "Normal Hour", 125.79f, 131.4f, 118.36f, 131.4f, 126.81f, 131.04f, 126.81f, 131.04f, 131.04f, 131.99f, 136.69f, 131.99f);

        ConsumptionDynamicDto normalHoursQ = getConsumptionDynamicDto("#0066cc", "Normal Hour", 125.79f, 131.4f, 118.36f, 131.4f);

        ConsumptionDynamicDto offHours = getConsumptionDynamicDto("#248f24", "Off Hours", 265.96f, 282.73f, 238.51f, 250.71f, 211.15f, 209.45f, 209f, 228.47f, 227.03f, 220.12f, 245.33f, 310.36f);

        ConsumptionDynamicDto offHoursQ = getConsumptionDynamicDto("#248f24", "Off Hours", 265.96f, 282.73f, 238.51f, 250.71f);

        boolean isQuarter = periodType.equals(TimePeriodType.QUARTERS);

        if (datePartType.equals(DatePartType.FREE_HOURS)) {
            return (isQuarter ? freeHoursQ : freeHours);
        } else if (datePartType.equals(DatePartType.NORMAL_HOURS)) {
            return (isQuarter ? normalHoursQ : normalHours);
        } else if (datePartType.equals(DatePartType.OFF_HOURS)) {
            return (isQuarter ? offHoursQ : offHours);
        } else if (datePartType.equals(DatePartType.PEAK_HOURS)) {
            return (isQuarter ? peakHoursQ : peakHours);
        }
        return null;
    }

    private ConsumptionDynamicDto getConsumptionDynamicDto(String s, String s2, float v, float v2, float v3, float v4) {
        ConsumptionDynamicDto freeHoursQ = new ConsumptionDynamicDto();
        freeHoursQ.setColor(s);
        freeHoursQ.setName(s2);
        freeHoursQ.setData(Arrays.asList(v, v2, v3, v4));
        return freeHoursQ;
    }

    private ConsumptionDynamicDto getConsumptionDynamicDto(String s, String s2, float v, float v2, float v3, float v4, float v5, float v6, float v7, float v8, float v9, float v10, float v11, float v12) {
        ConsumptionDynamicDto peakHours = new ConsumptionDynamicDto();
        peakHours.setColor(s);
        peakHours.setName(s2);
        peakHours.setData(Arrays.asList(v, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12));
        return peakHours;
    }

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

    public NormalPerCapitaDto getNormalizedPerCapita(String buildingId) {
        NormalPerCapitaDto dto = new NormalPerCapitaDto();
        dto.setXValues(Arrays.asList("Jan-2018", "Feb-2018", "Mar-2018", "Apr-2018", "May-2018", "Jun-2018", "Jul-2018", "Aug-2018", "Sept-2018", "Oct-2018", "Nov-2018", "Dec-2018"));
        dto.setBaseLine(Arrays.asList(98.13f, 98.13f, 98.13f, 98.13f, 98.13f, 98.13f, 98.13f, 98.13f, 98.13f, 98.13f, 98.13f, 98.13f));
        dto.setTotal(Arrays.asList(116.351f, 122.56f, 107.364f, 110.476f, 88.964f, 87.093f, 95.84f, 104.204f, 100.311f, 102.862f, 105.511f, 122.884f));
        return dto;
    }

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

    public PredictedWeatherVsRealDto getPredictedWeatherVSReal(String buildingId) {
        PredictedWeatherVsRealDto dto = new PredictedWeatherVsRealDto();
        dto.setXValues(Arrays.asList("Jan-2019", "Feb-2019", "Mar-2019"));
        dto.setBaseLineValues(Arrays.asList(10.62f, 9.85f, 9.38f));
        dto.setConsumptionValues(Arrays.asList(10.94f, 11.21f, 9.3f));
        return dto;
    }

    public CarbonPieDto getCarbonPieData(String buildingId) {
        CarbonPieDto dto = new CarbonPieDto();
        dto.setCo2Free(19.4f);
        dto.setCo2Normal(51f);
        dto.setCo2Off(11.4f);
        dto.setCo2Peak(18.1f);
        return dto;
    }

    public CarbonSPLineDto getCarbonSPLineData(String buildingId) {
        CarbonSPLineDto dto = new CarbonSPLineDto();
        dto.setTotalValues(Arrays.asList(4.28f, 4.51f, 3.95f, 4.07f, 3.27f, 3.2f, 3.53f, 3.83f, 3.69f, 3.78f, 3.88f, 4.52f));
        dto.setBaseLineValues(Arrays.asList(3.74f, 3.74f, 3.74f, 3.74f, 3.74f, 3.74f, 3.74f, 3.74f, 3.74f, 3.74f, 3.74f, 3.74f));
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
        return dto;
    }
}