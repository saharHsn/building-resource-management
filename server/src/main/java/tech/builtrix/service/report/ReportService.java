package tech.builtrix.service.report;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.builtrix.controller.report.PredictionDto;
import tech.builtrix.controller.report.SavingDto;

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
}