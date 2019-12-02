package tech.builtrix.service.bill;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.builtrix.dto.BillDto;
import tech.builtrix.dto.BillParameterDto;
import tech.builtrix.exception.BillParseException;
import tech.builtrix.parseEngine.PdfParser;
import tech.builtrix.util.TExtractDto;

import java.util.Date;
import java.util.Map;

/**
 * Created By sahar at 12/2/19
 */

@Component
@Slf4j
public class BillParser {
    private final PdfParser pdfParser;

    public BillParser(PdfParser pdfParser) {
        this.pdfParser = pdfParser;
    }

    public BillDto parseBill(String bucketName, String fileName) throws BillParseException {
        TExtractDto tExtractDto;
        try {
            tExtractDto = this.pdfParser.parseFile(bucketName, fileName);
        } catch (Exception e) {
            logger.error("Encounter error : " + e.getMessage() + " parsing bill : " + bucketName + "_" + fileName);
            throw new BillParseException();
        }
        Map<String, String> keyValueResult = tExtractDto.getKeyValueResult();
        String tablesResult = tExtractDto.getTablesResult();
        String address = "";
        Date fromDate = null;
        Date toDate = null;
        Float totalPayable = null;
        Float activeEnergyCost = null;
        Float producedCo2 = null;
        Float powerDemandCost = null;
        Float averageDailyConsumption = null;
        Date aEOffInitialDate = null;
        Date aEOffEndDate = null;
        Float aEOffCost = null;
        Float aEOffConsumption = null;
        Float aEOffTariffPrice = null;
        Float aEOffTotalTariffCost = null;
        BillParameterDto activeEnergyOffHours = new BillParameterDto(aEOffInitialDate,
                aEOffEndDate,
                aEOffCost,
                aEOffConsumption,
                aEOffTariffPrice,
                aEOffTotalTariffCost);

        Date aEFreeInitialDate = null;
        Date aEFreeEndDate = null;
        Float aEFreeCost = null;
        Float aEFreeConsumption = null;
        Float aEFreeTariffPrice = null;
        Float aEFreeTotalTariffCost = null;
        BillParameterDto activeEnergyFreeHours = new BillParameterDto(aEFreeInitialDate,
                aEFreeEndDate,
                aEFreeCost,
                aEFreeConsumption,
                aEFreeTariffPrice,
                aEFreeTotalTariffCost
        );

        Date aENormalInitialDate = null;
        Date aENormalEndDate = null;
        Float aENormalCost = null;
        Float aENormalConsumption = null;
        Float aENormalTariffPrice = null;
        Float aENormalTotalTariffCost = null;
        BillParameterDto activeEnergyNormalHours = new BillParameterDto(
                aENormalInitialDate,
                aENormalEndDate,
                aENormalCost,
                aENormalConsumption,
                aENormalTariffPrice,
                aENormalTotalTariffCost
        );

        Date aEPeakInitialDate = null;
        Date aEPeakEndDate = null;
        Float aEPeakCost = null;
        Float aEPeakConsumption = null;
        Float aEPeakTariffPrice = null;
        Float aEPeakTotalTariffCost = null;
        BillParameterDto activeEnergyPeakHours = new BillParameterDto(
                aEPeakInitialDate,
                aEPeakEndDate,
                aEPeakCost,
                aEPeakConsumption,
                aEPeakTariffPrice,
                aEPeakTotalTariffCost);

        Date rdPeakInitialDate = null;
        Date rdPeakEndDate = null;
        Float rdPeakCost = null;
        Float rdPeakConsumption = null;
        Float rdPeakTariffPrice = null;
        Float rdPeakTotalTariffCost = null;
        BillParameterDto redesPeakHours = new BillParameterDto(
                rdPeakInitialDate,
                rdPeakEndDate,
                rdPeakCost,
                rdPeakConsumption,
                rdPeakTariffPrice,
                rdPeakTotalTariffCost
        );

        Date rdCPInitialDate = null;
        Date rdCPEndDate = null;
        Float rdCPCost = null;
        Float rdCPConsumption = null;
        Float rdCPTariffPrice = null;
        Float rdCPTotalTariffCost = null;
        BillParameterDto redesContractedPower = new BillParameterDto(
                rdCPInitialDate,
                rdCPEndDate,
                rdCPCost,
                rdCPConsumption,
                rdCPTariffPrice,
                rdCPTotalTariffCost
        );

        Date rdRPInitialDate = null;
        Date rdRPEndDate = null;
        Float rdRPCost = null;
        Float rdRPConsumption = null;
        Float rdRPTariffPrice = null;
        Float rdRPTotalTariffCost = null;
        BillParameterDto redesReactivePower = new BillParameterDto(
                rdRPInitialDate,
                rdRPEndDate,
                rdRPCost,
                rdRPConsumption,
                rdRPTariffPrice,
                rdRPTotalTariffCost
        );
        BillDto bill = new BillDto(
                address,
                fromDate,
                toDate,
                totalPayable,
                activeEnergyCost,
                producedCo2,
                powerDemandCost,
                averageDailyConsumption,
                activeEnergyOffHours,
                activeEnergyFreeHours,
                activeEnergyNormalHours,
                activeEnergyPeakHours,
                redesPeakHours,
                redesContractedPower,
                redesReactivePower
        );
        return bill;
    }
}
