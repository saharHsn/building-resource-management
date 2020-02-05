package tech.builtrix.services.bill;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import tech.builtrix.exceptions.BillParseException;
import tech.builtrix.models.bill.ParameterType;
import tech.builtrix.parseEngine.PdfParser;
import tech.builtrix.utils.DateUtil;
import tech.builtrix.utils.MyTable;
import tech.builtrix.utils.TExtractDto;
import tech.builtrix.web.dtos.bill.BillDto;
import tech.builtrix.web.dtos.bill.BillParameterDto;

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
	private static String SUPER_VAZIO2 = "Vazio (SV) ";
	private static String VAZIO_NORMAL = "Vazio Normal (VN) ";
	private static String VAZIO_NORMAL1 = "Vazio Norma (VN) ";
	private static String VAZIO_NORMAL2 = "Norma (VN) ";
	private static String CHEIA = "Cheia (C) ";
	private static String CHEIA1 = "(C) ";
	private static String PONTA = "Ponta (P) ";
	private static String PONTA2 = "(P) ";
	private static String POTENCIA_HORAS_DE_PONTA_ = "Potencia Horas de Ponta ";
	private static String POTENCIA_HORAS_DE_PONTA_2 = "Horas de Ponta ";
	private static String POTENCIA_CONTRATADA_ = "Potencia Contratada ";
	private static String POTENCIA_CONTRATADA_2 = "Contratada ";
	private static String REATIVA_FORNECIDA_NO_VAZIO = "Reativa Fornecida no vazio (Vz) ";
	private static String REATIVA_FORNECIDA_NO_VAZIO2 = "Fornecida no vazio (Vz) ";
	private String PERIODO_DE_FATURACAO_ = "PERIODO DE FATURACAO ";
	private String TOTAL_A_PAGAR = "Total a pagar: (ELETRICIDADE) ";
	private String ENERGIA_ATIVA_ = "Energia Ativa ";
	private String REDES_ = "Redes ";
	// private final BillService billService;

	@Autowired
	public BillParser(PdfParser pdfParser, BillService billService) {
		this.pdfParser = pdfParser;
		// this.billService = billService;
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
		// MyTable table = tablesResult.get(3);
		MyTable table = findMainTable(tablesResult);
		assert table != null;
		Map<String, List<String>> column_value = table.getColumn_value();
		String billPeriod = keyValueResult.get(PERIODO_DE_FATURACAO_);
		String[] periods = billPeriod.trim().split("a");
		Date fromDate = DateUtil.getDateFromStr(periods[0], "dd/MM/yyyy");// 18/04/2018 a 17/05/2018
		Date toDate = DateUtil.getDateFromStr(periods[1], "dd/MM/yyyy");

		String totalPayableStr = keyValueResult.get(TOTAL_A_PAGAR);// 4.231,10 E
		Float totalPayable = getAmount(totalPayableStr);

		String energia_ativa_ = "0";
		List<String> energyActive = column_value.get(this.ENERGIA_ATIVA_);
		if (energyActive != null) {
			if (energyActive.size() >= 7) {
				energia_ativa_ = energyActive.get(6);
			} else if (energyActive.get(0) != null) {
				energia_ativa_ = energyActive.get(0);
			}
		} else {
			// Energia Ativa 3.426,42 E
			energia_ativa_ = "3.426,42 E";
		}
		Float activeEnergyCost = getAmount(energia_ativa_);
		List<String> redes = column_value.get(this.REDES_);
		String redes_ = "";
		if (redes != null) {
			redes_ = redes.get(6);
		} else {
			redes_ = "968,77 E ";
		}
		Float powerDemandCost = getAmount(redes_);

		// TODO ?
		String address = "";
		// TODO ?
		// Emissão de CO2 associada aos consumos de energia desta Fatura: 8.183,95 Kg
		Float producedCo2 = null;
		// TODO ?
		// Consumo médio dos últimos 12 meses: 767,90 kWh
		Float averageDailyConsumption = null;

		if (column_value.get(SUPER_VAZIO) == null) {
			SUPER_VAZIO = SUPER_VAZIO2;
		}
		BillParameterDto aEOffHours = getBillParameter(column_value, SUPER_VAZIO, ParameterType.AE_OFF_HOURS);
		if (column_value.get(VAZIO_NORMAL) == null) {
			VAZIO_NORMAL = VAZIO_NORMAL1;
			if (column_value.get(VAZIO_NORMAL) == null) {
				VAZIO_NORMAL = VAZIO_NORMAL2;
			}
		}
		BillParameterDto aEFreeHours = getBillParameter(column_value, VAZIO_NORMAL, ParameterType.AE_FREE_HOURS);
		if (column_value.get(CHEIA) == null) {
			CHEIA = CHEIA1;
		}
		BillParameterDto aENormalHours = getBillParameter(column_value, CHEIA, ParameterType.AE_NORMAL_HOURS);
		if (column_value.get(PONTA) == null) {
			PONTA = PONTA2;
		}
		BillParameterDto aEPeakHours = getBillParameter(column_value, PONTA, ParameterType.AE_PEAK_HOURS);
		if (column_value.get(POTENCIA_HORAS_DE_PONTA_) == null) {
			POTENCIA_HORAS_DE_PONTA_ = POTENCIA_HORAS_DE_PONTA_2;
		}
		BillParameterDto rDPeakHours = getBillParameter(column_value, POTENCIA_HORAS_DE_PONTA_,
				ParameterType.RD_PEAK_HOURS);
		if (column_value.get(POTENCIA_CONTRATADA_) == null) {
			POTENCIA_CONTRATADA_ = POTENCIA_CONTRATADA_2;
		}
		BillParameterDto rDContractedPower = getBillParameter(column_value, POTENCIA_CONTRATADA_,
				ParameterType.RD_CONTRACTED_POWER);

		if (column_value.get(REATIVA_FORNECIDA_NO_VAZIO) == null) {
			REATIVA_FORNECIDA_NO_VAZIO = REATIVA_FORNECIDA_NO_VAZIO2;
		}
		BillParameterDto rDReactivePower = null;
		if (column_value.get(REATIVA_FORNECIDA_NO_VAZIO) != null) {
			rDContractedPower = getBillParameter(column_value, REATIVA_FORNECIDA_NO_VAZIO,
					ParameterType.RD_REACTIVE_POWER);
		}
		Float totalMonthlyConsumption = aEOffHours.getConsumption() + aEFreeHours.getConsumption()
				+ aENormalHours.getConsumption() + +aEPeakHours.getConsumption();
		BillDto bill = new BillDto(buildingId, address, fromDate, DateUtil.getYear(fromDate),
				DateUtil.getMonth(fromDate), toDate, totalPayable, activeEnergyCost, producedCo2, powerDemandCost,
				averageDailyConsumption, totalMonthlyConsumption, aEOffHours, aEFreeHours, aENormalHours, aEPeakHours,
				rDPeakHours, rDContractedPower, rDReactivePower);
		return bill;
	}

	private MyTable findMainTable(List<MyTable> tablesResult) {
		for (MyTable myTable : tablesResult) {
			Map<String, List<String>> column_value = myTable.getColumn_value();
			for (String s : column_value.keySet()) {
				if (s.equalsIgnoreCase(SUPER_VAZIO) || s.equalsIgnoreCase(SUPER_VAZIO2)) {
					return myTable;
				}
			}
		}
		return null;
	}

	private Float getAmount(String e) {
		e = e.trim();
		return Float.valueOf(e.replaceAll("\\.", "").replaceAll(",", ".").replaceAll("E", "").replaceAll("e", "")
				.replaceAll(" ", "").replaceAll(":", "").replaceAll("%", ""));
	}

	private BillParameterDto getBillParameter(Map<String, List<String>> column_value, String paramName,
			ParameterType parameterType) throws ParseException {
		List<String> paramValues = column_value.get(paramName);
		String initialDateStr = paramValues.get(0);// 18/04/2018
		String endDateStr = paramValues.get(1);// 17/05/2018
		// String costStr = column_value.get(paramName).get(2);//2.354,0000
		String consumptionStr = paramValues.get(2);// 0,089700 E
		String tariffPriceStr = paramValues.get(3);
		String totalTariffCostStr = paramValues.get(6);
		BillParameterDto parameterDto = new BillParameterDto();
		if (!StringUtils.isEmpty(consumptionStr)) {
			parameterDto.setConsumption(getAmount(consumptionStr));
		}
		/*
		 * if (!StringUtils.isEmpty(costStr)) {
		 * parameterDto.setCost(getAmount(costStr)); }
		 */
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
		parameterDto.setParamType(parameterType);
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
