package tech.builtrix.services.bill;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tech.builtrix.exceptions.BillParseException;
import tech.builtrix.models.bill.enums.ParameterType;
import tech.builtrix.models.building.enums.ElectricityBillType;
import tech.builtrix.parseEngine.PdfParser;
import tech.builtrix.utils.DateUtil;
import tech.builtrix.utils.MyTable;
import tech.builtrix.utils.TExtractDto;
import tech.builtrix.web.dtos.bill.BillDto;
import tech.builtrix.web.dtos.bill.BillParameterDto;

import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created By sahar at 12/2/19
 */

@Component
@Slf4j
public class BillParser {
    // private static final float CO2_PRODUCTION_RATE = 0.251f;
    //kg/kWh;
    private static Dictionary<String, String> PARAMS_DICTIONARY;
    private static String SUPER_VAZIO = "Super Vazio (SV) ";
    private static String REDES_SUPER_VAZIO = "Redes Super Vazio (SV) ";
    private static String SUPER_VAZIO2 = "Vazio (SV) ";
    private static String CONSUMO_ESTIMADO = "Simples - Consumo estimado ";
    private static String VAZIO_NORMAL = "Vazio Normal (VN) ";
    private static String VAZIO_CONSUMO_ESTIMADO = "Vazio – Consumo Estimado ";
    private static String REDES_VAZIO_CONSUMO_ESTIMADO = "Redes Vazio – Consumo Estimado ";
    private static String REDES_VAZIO_NORMAL = "Redes Vazio Normal (VN) ";
    private static String VAZIO_NORMAL1 = "Vazio Norma (VN) ";
    private static String VAZIO_NORMAL2 = "Norma (VN) ";
    private static String SIMPLES_CONSUMO_JA_FACTURADO = "Simples Consumo ja facturado ";
    private static String SIMPLES_CONSUMO_MEDIDO = "Simples Consumo medido ";
    private static String SIMPLES_CONSUMO_ESTIMADO = "Simples - Consumo estimado ";
    private static String SIMPLES_CONSUMO_ESTIMADO2 = "Simples Consumo estimado ";
    private static String CHEIA = "Cheia (C) ";
    private static String REDES_CHEIA = "Redes Cheia (C) ";
    private static String CHEIA_CONSUMO_ESTIMADO = "Cheia - Consumo estimado ";
    private static String REDES_CHEIA_CONSUMO_ESTIMADO = "Redes Cheia - Consumo estimado ";
    private static String CHEIA1 = "(C) ";
    private static String PONTA = "Ponta (P) ";
    private static String REDES_PONTA = "Redes Ponta (P) ";
    private static String PONTA_CONSUMO_ESTIMADO = "Ponta – Consumo Estimado ";
    private static String REDES_PONTA_CONSUMO_ESTIMADO = "Redes Ponta - Consumo estimado ";
    private static String POTENCIA_HORAS_DE_PONTA = "Potência Horas de Ponta";
    private static String POTENCIA_HORAS_DE_PONTA_2 = "Horas de Ponta ";
    // private static String POTENCIA_CONTRATADA_ = "Potencia Contratada ";
    private static String CONTRACTED_POWER_0 = "Contratada ";
    private String CONTRACTED_POWER = "Potência Contratada";
    private static String REATIVA_FORNECIDA_NO_VAZIO = "Reativa Fornecida no vazio (Vz) ";
    private static String REATIVA_FORNECIDA_NO_VAZIO2 = "Fornecida no vazio (Vz) ";
    private final PdfParser pdfParser;
    private String PERIODO_DE_FATURACAO_ = "PERIODO DE FATURACAO ";
    private String TOTAL_A_PAGAR = "Total a pagar: (ELETRICIDADE) ";
    private String ENERGIA_ATIVA_ = "Energia Ativa ";
    private String REDES_ = "Redes ";
    private String ELECTRICITY_COUNTER_CODE = "CÓDIGO PONTO ENTREGA ELETRICIDADE";
    private String COMPANY_TAX_NUMBER = "Numero ID. Fiscal: ";

    @Autowired
    public BillParser(PdfParser pdfParser) {
        this.pdfParser = pdfParser;
        // this.billService = billService;
    }

    public static void main(String[] args) {
        String str = "Redes Super Vazio (SV) ";
        String pattern = str + "-row(.*)";
        Pattern r = Pattern.compile(pattern);
        String input = "Redes Super Vazio (SV) -row10";
        Matcher m = r.matcher(input);
        if (m.find()) {
            System.out.println("find");
        }
    }

    public BillDto parseBill(String buildingId, String bucketName, String fileName)
            throws BillParseException, ParseException {
        TExtractDto tExtractDto;
        try {
            tExtractDto = this.pdfParser.parseFile(bucketName, fileName);
        } catch (Exception e) {
            logger.error("Encounter error : " + e.getMessage() + " parsing bill : " + bucketName + "_" + fileName);
            throw new BillParseException();
        }
        Map<String, String> keyValueResult = tExtractDto.getKeyValueResult();
        List<MyTable> tablesResult = tExtractDto.getTablesResult();

        String electricityCounterCode = keyValueResult.get(ELECTRICITY_COUNTER_CODE);

        String billPeriod = keyValueResult.get(PERIODO_DE_FATURACAO_);
        String[] periods = billPeriod.trim().split("a");
        Date fromDate = DateUtil.getDateFromStr(periods[0], "dd/MM/yyyy");// 18/04/2018 a 17/05/2018
        Date toDate = DateUtil.getDateFromStr(periods[1], "dd/MM/yyyy");
        //TODO check if totalPayable is negative value or not in order to notify user about it
        MyTable firstTable = findTableWithRowHeaders(tablesResult, ENERGIA_ATIVA_, REDES_);
        List<String> energyAtiviaValues = firstTable.getColumn_value().get(ENERGIA_ATIVA_);
        List<String> redesValues = firstTable.getColumn_value().get(REDES_);
        //String totalPayableStr = keyValueResult.get(ENERGIA_ATIVA_) + keyValueResult.get(REDES_);
        Float totalPayable = getAmount(energyAtiviaValues.get(0)) + getAmount(redesValues.get(0));

        String companyTaxNumberStr = keyValueResult.get(COMPANY_TAX_NUMBER);


        String address = "";
        float averageDailyConsumption = 0f;

        float totalMonthlyConsumption = 0;
        MyTable table = findTableWithRowHeaders(tablesResult, SUPER_VAZIO, SUPER_VAZIO2, CONSUMO_ESTIMADO);
        Map<String, List<String>> column_value = table != null ? table.getColumn_value() : null;
        String energia_ativa_ = "0";
        List<String> energyActive = column_value != null ? column_value.get(this.ENERGIA_ATIVA_) : null;
        if (energyActive != null) {
            if (energyActive.size() >= 7) {
                energia_ativa_ = energyActive.get(6);
            } else if (energyActive.get(0) != null) {
                energia_ativa_ = energyActive.get(0);
            }
        } else {
            energia_ativa_ = "3.426,42 E";
        }
        Float activeEnergyCost = getAmount(energia_ativa_);
        List<String> redes = column_value != null ? column_value.get(this.REDES_) : null;
        String redes_;
        if (redes != null) {
            redes_ = redes.get(6);
        } else {
            redes_ = "968,77 E ";
        }
        Float powerDemandCost = getAmount(redes_);


        ElectricityBillType electricityBillType;
        electricityBillType = detectBillType(table);

        BillParameterDto aEFreeHours = null;
        BillParameterDto rDOffHours = null;
        BillParameterDto rDFreeHours = null;
        BillParameterDto aENormalHours = null;
        BillParameterDto rDNormalHours = null;
        BillParameterDto aEPeakHours = null;
        BillParameterDto rDPeakHours = null;
        BillParameterDto rDContractedPower = null;
        BillParameterDto rDReactivePower = null;
        BillParameterDto aEOffHours = null;
        if (column_value != null) {
            aEOffHours = getBillParameter(column_value, SUPER_VAZIO, ParameterType.AE_OFF_HOURS);
            rDOffHours = getBillParameter(column_value, REDES_SUPER_VAZIO, ParameterType.RD_OFF_HOURS);
            aEFreeHours = getBillParameter(column_value, VAZIO_NORMAL, ParameterType.AE_FREE_HOURS);
            if (aEFreeHours == null) {
                aEFreeHours = getBillParameter(column_value, VAZIO_CONSUMO_ESTIMADO, ParameterType.AE_FREE_HOURS);
            }

            rDFreeHours = getBillParameter(column_value, REDES_VAZIO_NORMAL, ParameterType.RD_FREE_HOURS);
            if (rDFreeHours == null) {
                rDFreeHours = getBillParameter(column_value, REDES_VAZIO_CONSUMO_ESTIMADO, ParameterType.RD_FREE_HOURS);
            }

            aENormalHours = getBillParameter(column_value, CHEIA, ParameterType.AE_NORMAL_HOURS);
            if (aENormalHours == null) {
                aENormalHours = getBillParameter(column_value, CHEIA_CONSUMO_ESTIMADO, ParameterType.AE_NORMAL_HOURS);
            }

            rDNormalHours = getBillParameter(column_value, REDES_CHEIA, ParameterType.RD_NORMAL_HOURS);
            if (rDNormalHours == null) {
                rDNormalHours = getBillParameter(column_value, REDES_CHEIA_CONSUMO_ESTIMADO, ParameterType.RD_NORMAL_HOURS);
            }

            aEPeakHours = getBillParameter(column_value, PONTA, ParameterType.AE_PEAK_HOURS);
            if (aEPeakHours == null) {
                aEPeakHours = getBillParameter(column_value, PONTA_CONSUMO_ESTIMADO, ParameterType.AE_PEAK_HOURS);
            }

            rDPeakHours = getBillParameter(column_value, REDES_PONTA, ParameterType.RD_PEAK_HOURS);
            if (rDPeakHours == null) {
                rDPeakHours = getBillParameter(column_value, REDES_PONTA_CONSUMO_ESTIMADO, ParameterType.RD_PEAK_HOURS);
            }

            rDContractedPower = getBillParameter(column_value, REATIVA_FORNECIDA_NO_VAZIO, ParameterType.RD_REACTIVE_POWER);

            totalMonthlyConsumption = getTotalConsumptionValue(column_value);

        }
        int year = DateUtil.getYear(fromDate);
        int month = DateUtil.getMonth(fromDate);
        float CO2_PRODUCTION_RATE = getCO2ProductionRate(year);
        float producedCo2 = totalMonthlyConsumption * CO2_PRODUCTION_RATE;

        BillDto bill = new BillDto(buildingId,
                electricityCounterCode,
                companyTaxNumberStr,
                address,
                fromDate,
                toDate,
                year,
                month,
                totalPayable,
                activeEnergyCost,
                producedCo2,
                powerDemandCost,
                averageDailyConsumption,
                totalMonthlyConsumption,
                aEFreeHours,
                rDFreeHours,
                rDOffHours,
                aEOffHours,
                aENormalHours,
                rDNormalHours,
                aEPeakHours,
                rDPeakHours,
                rDContractedPower,
                rDReactivePower
        );
        return bill;
    }

    private Float getCO2ProductionRate(int year) {
        /* 2020: 0.251 kg/kWh
           2019: 0.330 kg/kWh
           2018: 0.400 kg/kWh
         */
        if (year == 2018) {
            return 0.400f;
        } else if (year == 2019) {
            return 0.330f;
        } else if (year == 2020) {
            return 0.251f;
        }
        return 0.25f;
    }

    private ElectricityBillType detectBillType(MyTable table) {
        return ElectricityBillType.FOUR_TARIFF;
    }

    private MyTable findTableWithRowHeaders(List<MyTable> tablesResult, String... rowHeaders) {
        for (MyTable myTable : tablesResult) {
            Map<String, List<String>> column_value = myTable.getColumn_value();
            for (String s : column_value.keySet()) {
                for (String rowHeader : rowHeaders) {
                    if (s.equalsIgnoreCase(rowHeader)) {
                        return myTable;
                    }
                }
              /*  if (s.equalsIgnoreCase(SUPER_VAZIO) || s.equalsIgnoreCase(SUPER_VAZIO2) || s.equalsIgnoreCase(CONSUMO_ESTIMADO)) {
                    return myTable;
                }*/
            }
        }
        return null;
    }

    private Float getAmount(String e) {
        e = e.trim();
        return Float.valueOf(e.replaceAll("\\.", "")
                .replaceAll(",", ".")
                .replaceAll("E", "")
                .replaceAll("e", "")
                .replaceAll(" ", "")
                .replaceAll(":", "")
                .replaceAll("\\{", "")
                .replaceAll("}", "")
                .replaceAll("%", ""));
    }

    private Float getTotalConsumptionValue(Map<String, List<String>> column_value) {
        List<String> superVazioParams = column_value.get(SUPER_VAZIO);
        List<String> vazioNormalParams = column_value.get(VAZIO_NORMAL);
        List<String> pontaParams = column_value.get(PONTA);
        List<String> cheiaParams = column_value.get(CHEIA);
        List<List<String>> similarSVRows = findAllSimilarRows(column_value, SUPER_VAZIO);
        List<List<String>> similarVNRows = findAllSimilarRows(column_value, VAZIO_NORMAL);
        List<List<String>> similarPRows = findAllSimilarRows(column_value, PONTA);
        List<List<String>> similarCRows = findAllSimilarRows(column_value, CHEIA);
        if (!CollectionUtils.isEmpty(similarSVRows) && !CollectionUtils.isEmpty(similarVNRows)
                && !CollectionUtils.isEmpty(similarPRows) && !CollectionUtils.isEmpty(similarCRows)) {
            float superVazio = 0f;
            float vazioNormal = 0f;
            float ponta = 0f;
            float cheia = 0f;
            for (List<String> similarRow : similarSVRows) {
                superVazio += getAmount(similarRow.get(2));
            }
            for (List<String> similarRow : similarVNRows) {
                vazioNormal += getAmount(similarRow.get(2));
            }
            for (List<String> similarRow : similarPRows) {
                ponta += getAmount(similarRow.get(2));
            }
            for (List<String> similarRow : similarCRows) {
                cheia += getAmount(similarRow.get(2));
            }
            return superVazio + vazioNormal + ponta + cheia;
            /*String superVazioStr = superVazioParams.get(2);
            String vazioNormalStr = vazioNormalParams.get(2);
            String pontaStr = pontaParams.get(2);
            String cheiaStr = cheiaParams.get(2);
            return getAmount(superVazioStr) + getAmount(vazioNormalStr) + getAmount(pontaStr) + getAmount(cheiaStr);*/
        } else {
            List<String> simpleConsumeEstimateParams = column_value.get(SIMPLES_CONSUMO_ESTIMADO);
            List<String> simpleConsumeFactorParams = column_value.get(SIMPLES_CONSUMO_JA_FACTURADO);
            List<String> simpleConsumeMedidoParams = column_value.get(SIMPLES_CONSUMO_MEDIDO);
            String estimadoStr = "0";
            if (!CollectionUtils.isEmpty(simpleConsumeEstimateParams)) {
                estimadoStr = simpleConsumeEstimateParams.get(2);
            }
            String factorStr = "0";
            if (!CollectionUtils.isEmpty(simpleConsumeFactorParams)) {
                factorStr = simpleConsumeFactorParams.get(2);
            }
            String medidoStr = "0";
            if (!CollectionUtils.isEmpty(simpleConsumeMedidoParams)) {
                medidoStr = simpleConsumeMedidoParams.get(2);
            }
            return getAmount(estimadoStr) + getAmount(factorStr) + getAmount(medidoStr);
        }
    }

    private Long getTotalConsumptionValueBillType3(Map<String, List<String>> column_value) {
        //TODo TEST for Multiple values
        List<String> simpleConsumeEstimateParams = column_value.get(SIMPLES_CONSUMO_ESTIMADO);
        List<String> simpleConsumeFactorParams = column_value.get(SIMPLES_CONSUMO_JA_FACTURADO);
        List<String> simpleConsumeMedidoParams = column_value.get(SIMPLES_CONSUMO_MEDIDO);
        String estimadoStr = simpleConsumeEstimateParams.get(2);
        String factorStr = simpleConsumeFactorParams.get(2);
        String medidoStr = simpleConsumeMedidoParams.get(2);
        return Long.valueOf((estimadoStr + factorStr + medidoStr));
    }

    private BillParameterDto getBillParameter(Map<String, List<String>> column_value,
                                              String paramName,
                                              ParameterType parameterType) throws ParseException {
        List<List<String>> similarRows = findAllSimilarRows(column_value, paramName);
        BillParameterDto billParameterDto = mergeSimilarRowInfo(similarRows);
        if (billParameterDto != null) {
            billParameterDto.setParamType(parameterType);
        }
        return billParameterDto;
    }

    private BillParameterDto mergeSimilarRowInfo(List<List<String>> similarRows) throws ParseException {
        BillParameterDto billParameterDto = null;
        for (List<String> similarRow : similarRows) {
            if (!CollectionUtils.isEmpty(similarRow)) {
                if (billParameterDto == null) {
                    billParameterDto = new BillParameterDto();
                }
                String initialDateStr = similarRow.get(0);// 18/04/2018
                String endDateStr = similarRow.get(1);// 17/05/2018
                String consumptionStr = similarRow.get(2);// 0,089700 E
                String tariffPriceStr = similarRow.get(3);
                String totalTariffCostStr = similarRow.get(6);
                // BillParameterDto parameterDto = new BillParameterDto();
                if (!StringUtils.isEmpty(consumptionStr)) {
                    billParameterDto.setConsumption(billParameterDto.getConsumption() + getAmount(consumptionStr));
                }
                if (!StringUtils.isEmpty(initialDateStr)) {
                    billParameterDto.setInitialDate(DateUtil.getDateFromStr(initialDateStr, "dd/MM/yyyy"));
                }
                if (!StringUtils.isEmpty(endDateStr)) {
                    billParameterDto.setEndDate(DateUtil.getDateFromStr(endDateStr, "dd/MM/yyyy"));
                }
                if (!StringUtils.isEmpty(tariffPriceStr)) {
                    billParameterDto.setTariffPrice(billParameterDto.getTariffPrice() + getAmount(tariffPriceStr));
                }
                if (!StringUtils.isEmpty(totalTariffCostStr)) {
                    billParameterDto.setTotalTariffCost(billParameterDto.getTotalTariffCost() + getAmount(totalTariffCostStr));
                }
            }
        }
        return billParameterDto;
    }

    private List<List<String>> findAllSimilarRows(Map<String, List<String>> column_value, String paramName) {
        List<List<String>> similarRows = new ArrayList<>();
        List<String> collection = column_value.get(paramName);
        if (!CollectionUtils.isEmpty(collection)) {
            similarRows.add(collection);
        }
        for (String key : column_value.keySet()) {
            if (key.startsWith(paramName + "-row")) {
                similarRows.add(column_value.get(key));
            }
        }
        return similarRows;
    }

}
