package tech.builtrix.services.bill;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import tech.builtrix.dtos.bill.BillDto;
import tech.builtrix.dtos.bill.BillParameterDto;
import tech.builtrix.exceptions.BillParseException;
import tech.builtrix.parseEngine.PdfParser;
import tech.builtrix.utils.DateUtil;
import tech.builtrix.utils.MyTable;
import tech.builtrix.utils.TExtractDto;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created By sahar at 12/2/19
 */

@Component
@Slf4j
public class BillParser {
    private final PdfParser pdfParser;
    private static String SUPER_VAZIO = "Super Vazio (SV) ";
    private static String VAZIO_NORMAL = "Vazio Norma (VN) ";
    private static String CHEIA = "Cheia (C) ";
    private static String PONTA = "Ponta (P) ";
    private static String POTENCIA_HORAS_DE_PONTA_ = "Potencia Horas de Ponta ";
    private static String POTENCIA_CONTRATADA_ = "Potencia Contratada ";
    private static String REATIVA_FORNECIDA_NO_VAZIO = "Reativa Fornecida no vazio (Vz) ";
    private String PERIODO_DE_FATURACAO_ = "PERIODO DE FATURACAO ";
    private String TOTAL_A_PAGAR = "Total a pagar: (ELETRICIDADE) ";
    private String ENERGIA_ATIVA_ = "Energia Ativa ";
    private String REDES_ = "Redes ";
    private final BillService billService;

    @Autowired
    public BillParser(PdfParser pdfParser, BillService billService) {
        this.pdfParser = pdfParser;
        this.billService = billService;
    }

    public BillDto parseBill(String buildingId, String bucketName, String fileName) throws BillParseException, ParseException {
        TExtractDto tExtractDto;
        try {
            tExtractDto = this.pdfParser.parseFile(bucketName, fileName);
        } catch (Exception e) {
            logger.error("Encounter error : " + e.getMessage() + " parsing bill : " + bucketName + "_" + fileName);
            throw new BillParseException();
        }
        Map<String, String> keyValueResult = tExtractDto.getKeyValueResult();
        List<MyTable> tablesResult = tExtractDto.getTablesResult();
        MyTable table = tablesResult.get(3);
        Map<String, List<String>> column_value = table.getColumn_value();
        String billPeriod = keyValueResult.get(PERIODO_DE_FATURACAO_);
        String[] periods = billPeriod.trim().split("a");
        Date fromDate = DateUtil.getDateFromStr(periods[0], "dd/MM/yyyy");// 18/04/2018 a 17/05/2018
        Date toDate = DateUtil.getDateFromStr(periods[1], "dd/MM/yyyy");

        String totalPayableStr = keyValueResult.get(TOTAL_A_PAGAR);//4.231,10 E
        Float totalPayable = getAmount(totalPayableStr);

        String energia_ativa_ = column_value.get(this.ENERGIA_ATIVA_).get(6);
        Float activeEnergyCost = getAmount(energia_ativa_);
        String redes_ = column_value.get(this.REDES_).get(6);
        Float powerDemandCost = getAmount(redes_);

        // TODO ?
        String address = "";
        //TODO ?
        //Emissão de CO2 associada aos consumos de energia desta Fatura: 8.183,95 Kg
        Float producedCo2 = null;
        //TODO ?
        //Consumo médio dos últimos 12 meses: 767,90 kWh
        Float averageDailyConsumption = null;

        BillParameterDto aEOffHours = getBillParameter(column_value, SUPER_VAZIO);
        BillParameterDto aEFreeHours = getBillParameter(column_value, VAZIO_NORMAL);
        BillParameterDto aENormalHours = getBillParameter(column_value, CHEIA);
        BillParameterDto aEPeakHours = getBillParameter(column_value, PONTA);
        BillParameterDto rDPeakHours = getBillParameter(column_value, POTENCIA_HORAS_DE_PONTA_);
        BillParameterDto rDContractedPower = getBillParameter(column_value, POTENCIA_CONTRATADA_);
        BillParameterDto rDReactivePower = getBillParameter(column_value, REATIVA_FORNECIDA_NO_VAZIO);
        BillDto bill = new BillDto(buildingId,
                address,
                fromDate,
                DateUtil.getYear(fromDate),
                DateUtil.getMonth(fromDate),
                toDate,
                totalPayable,
                activeEnergyCost,
                producedCo2,
                powerDemandCost,
                averageDailyConsumption,
                aEOffHours,
                aEFreeHours,
                aENormalHours,
                aEPeakHours,
                rDPeakHours,
                rDContractedPower,
                rDReactivePower
        );
        this.billService.save(bill);
        return bill;
    }

    private Float getAmount(String e) {
        return Float.valueOf(e.replaceAll("\\.", "")
                .replaceAll(",", ".")
                .replaceAll("E", "")
                .replaceAll("e", "")
                .replaceAll("%", ""));
    }

    private BillParameterDto getBillParameter(Map<String, List<String>> column_value, String paramName) throws ParseException {
        List<String> paramValues = column_value.get(paramName);
        String initialDateStr = paramValues.get(0);//18/04/2018
        String endDateStr = paramValues.get(1);//17/05/2018
        //String costStr = column_value.get(paramName).get(2);//2.354,0000
        String consumptionStr = paramValues.get(2);//0,089700 E
        String tariffPriceStr = paramValues.get(3);
        String totalTariffCostStr = paramValues.get(6);
        BillParameterDto parameterDto = new BillParameterDto();
        if (!StringUtils.isEmpty(consumptionStr)) {
            parameterDto.setConsumption(getAmount(consumptionStr));
        }
        /*if (!StringUtils.isEmpty(costStr)) {
            parameterDto.setCost(getAmount(costStr));
        }*/
        if (!StringUtils.isEmpty(initialDateStr)) {
            parameterDto.setInitialDate(DateUtil.getDateFromStr(initialDateStr, "dd/MM/yyyy"));
        }
        if (!StringUtils.isEmpty(endDateStr)) {
            parameterDto.setEndDate(DateUtil.getDateFromStr(endDateStr, "dd/MM/yyyy"));
        }
        if (!StringUtils.isEmpty(tariffPriceStr)) {
            parameterDto.setTariffPrice(getAmount(tariffPriceStr));
        }
        if (!StringUtils.isEmpty(totalTariffCostStr)) {
            parameterDto.setTotalTariffCost(getAmount(totalTariffCostStr));
        }
        return parameterDto;
    }

    public static void main(String[] args) {
        String period = " 18/04/2018 a 17/05/2018";
        String[] as = period.trim().split("a");
        for (String a : as) {
            System.out.println(a);
        }
    }
}
