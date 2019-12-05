package tech.builtrix.service.report;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.builtrix.controller.report.*;

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
}