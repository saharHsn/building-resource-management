package tech.builtrix.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tech.builtrix.models.building.EnergyCertificate;
import tech.builtrix.web.dtos.bill.BillDto;
import tech.builtrix.web.dtos.report.ConsumptionDto;
import tech.builtrix.web.dtos.report.ConsumptionDynamicDto;
import tech.builtrix.web.dtos.report.CostStackDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created By sahar at 12/11/19
 */
public class ReportUtil {
    public static Float getRankOfBuilding(EnergyCertificate energyCertificate) {
        // TODO read values from National DB
        CertificateInfo aPlus = new CertificateInfo(1, 1.8f, EnergyCertificate.APlus);
        CertificateInfo a = new CertificateInfo(2, 8.3f, EnergyCertificate.A);
        CertificateInfo b = new CertificateInfo(3, 9.7f, EnergyCertificate.B);
        CertificateInfo bMinus = new CertificateInfo(4, 9.7f, EnergyCertificate.BMinus);
        CertificateInfo c = new CertificateInfo(5, 26.4f, EnergyCertificate.C);
        CertificateInfo d = new CertificateInfo(6, 22.8f, EnergyCertificate.D);
        CertificateInfo e = new CertificateInfo(7, 13.5f, EnergyCertificate.E);
        CertificateInfo f = new CertificateInfo(8, 7.8f, EnergyCertificate.F);
        List<CertificateInfo> certificateInfoList = Arrays.asList(aPlus, a, b, bMinus, c, d, e, f);
        CertificateInfo certificateInfo = findCertificateInfo(energyCertificate, certificateInfoList);
        Float sum = 0f;
        for (CertificateInfo info : certificateInfoList) {
            if (info.getRank() <= certificateInfo.getRank()) {
                sum += info.getValue();
            }
        }
        return sum;
    }

    private static CertificateInfo findCertificateInfo(EnergyCertificate energyCertificate,
                                                       List<CertificateInfo> certificateInfoList) {
        for (CertificateInfo certificateInfo : certificateInfoList) {
            if (certificateInfo.getCertificateType().equals(energyCertificate))
                return certificateInfo;
        }
        return null;
    }

    public static String getDateTitle(Integer month, int year) {
        return DateUtil.getMonth(month) + "-" + year;
    }

    public static List<Float> getSavings(Float month1Cost, Float month2Cost, Float month3Cost, int billsSize) {
        Float savings1 = 0.05f * (month1Cost / billsSize);
        Float savings2 = 0.05f * (month2Cost / billsSize);
        Float savings3 = 0.05f * month3Cost / billsSize;
        return Arrays.asList(savings1, savings2, savings3);
    }

    public static Date getCurrentDate() {
        return new Date();
    }

    public static CostStackDto getCostStackDto(List<BillDto> dtoList) {
        CostStackDto dto = new CostStackDto();
        List<Float> contractedPowerValues = new ArrayList<>();
        List<Float> powerInPeakValues = new ArrayList<>();
        List<Float> reactivePowerValues = new ArrayList<>();
        List<Float> normalValues = new ArrayList<>();
        List<Float> peakValues = new ArrayList<>();
        List<Float> freeValues = new ArrayList<>();
        List<Float> offValues = new ArrayList<>();
        extractCostValues(dtoList, contractedPowerValues,
                powerInPeakValues,
                reactivePowerValues,
                normalValues,
                peakValues,
                freeValues,
                offValues);
        dto.setContractedPowerValues(contractedPowerValues);
        dto.setFreeValues(freeValues);
        dto.setOffValues(offValues);
        dto.setPeakValues(peakValues);
        dto.setPowerInPeakValues(powerInPeakValues);
        dto.setNormalValues(normalValues);
        dto.setReactivePowerValues(reactivePowerValues);
        //TODO refine later
        dto.setXValues(Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"));
        return dto;
    }

    private static void extractCostValues(List<BillDto> dtoList, List<Float> contractedPowerValues, List<Float> powerInPeakValues, List<Float> reactivePowerValues, List<Float> normalValues, List<Float> peakValues, List<Float> freeValues, List<Float> offValues) {
        for (BillDto billDto : dtoList) {
            if (billDto.getRDContractedPower() != null) {
                contractedPowerValues.add(billDto.getRDContractedPower().getTotalTariffCost());
            }
            if (billDto.getAEFreeHours() != null) {
                freeValues.add(billDto.getAEFreeHours().getTotalTariffCost());
            }
            if (billDto.getAEOffHours() != null) {
                offValues.add(billDto.getAEOffHours().getTotalTariffCost());
            }
            if (billDto.getAEPeakHours() != null) {
                peakValues.add(billDto.getAEPeakHours().getTotalTariffCost());
            }
            if (billDto.getRDPeakHours() != null) {
                powerInPeakValues.add(billDto.getRDPeakHours().getTotalTariffCost());
            }
            if (billDto.getAENormalHours() != null) {
                normalValues.add(billDto.getAENormalHours().getTotalTariffCost());
            }
            if (billDto.getRDReactivePower() != null) {
                reactivePowerValues.add(billDto.getRDReactivePower().getTotalTariffCost());
            }
        }
    }

    public static ConsumptionDto getConsumptionDto(List<BillDto> dtoList) {
        ConsumptionDto dto = new ConsumptionDto();
        //TODO refine later
        dto.setXValues(Arrays.asList("Jan-2018", "Feb-2018", "Mar-2018", "Apr-2018", "May-2018", "Jun-2018",
                "Jul-2018", "Aug-2018", "Sept-2018", "Oct-2018", "Nov-2018", "Dec-2018"));

        List<Float> contractedPowerValues = new ArrayList<>();
        List<Float> powerInPeakValues = new ArrayList<>();
        List<Float> reactivePowerValues = new ArrayList<>();
        List<Float> normalValues = new ArrayList<>();
        List<Float> peakValues = new ArrayList<>();
        List<Float> freeValues = new ArrayList<>();
        List<Float> offValues = new ArrayList<>();
        extractConsumptionValues(dtoList,
                contractedPowerValues,
                powerInPeakValues,
                reactivePowerValues,
                normalValues,
                peakValues,
                freeValues,
                offValues);
        dto.setContractedPowerValues(contractedPowerValues);
        dto.setFreeValues(freeValues);
        dto.setOffValues(offValues);
        dto.setPeakValues(peakValues);
        dto.setPowerInPeakValues(powerInPeakValues);
        dto.setNormalValues(normalValues);
        dto.setReactivePowerValues(reactivePowerValues);
        return dto;
    }

    private static void extractConsumptionValues(List<BillDto> dtoList,
                                                 List<Float> contractedPowerValues,
                                                 List<Float> powerInPeakValues,
                                                 List<Float> reactivePowerValues,
                                                 List<Float> normalValues,
                                                 List<Float> peakValues,
                                                 List<Float> freeValues,
                                                 List<Float> offValues) {
        for (BillDto billDto : dtoList) {
            if (billDto.getRDContractedPower() != null) {
                contractedPowerValues.add(billDto.getRDContractedPower().getConsumption());
            }
            if (billDto.getAEFreeHours() != null) {
                freeValues.add(billDto.getAEFreeHours().getConsumption());
            }
            if (billDto.getAEOffHours() != null) {
                offValues.add(billDto.getAEOffHours().getConsumption());
            }
            if (billDto.getAEPeakHours() != null) {
                peakValues.add(billDto.getAEPeakHours().getConsumption());
            }
            if (billDto.getRDPeakHours() != null) {
                powerInPeakValues.add(billDto.getRDPeakHours().getConsumption());
            }
            if (billDto.getAENormalHours() != null) {
                normalValues.add(billDto.getAENormalHours().getConsumption());
            }
            if (billDto.getRDReactivePower() != null) {
                reactivePowerValues.add(billDto.getRDReactivePower().getConsumption());
            }
        }
    }

    //TODO reimplement later
    public static ConsumptionDynamicDto getConsumptionDynamicDto(String s, String s2, float v, float v2, float v3, float v4) {
        ConsumptionDynamicDto freeHoursQ = new ConsumptionDynamicDto();
        freeHoursQ.setColor(s);
        freeHoursQ.setName(s2);
        freeHoursQ.setData(Arrays.asList(v, v2, v3, v4));
        return freeHoursQ;
    }

   /* public Integer getCurrentYear() {
        Date currentDate = getCurrentDate();
        return DateUtil.getYear(currentDate);
    }*/

    public static ConsumptionDynamicDto getConsumptionDynamicDto(String color, String name, List<Float> data) {
        ConsumptionDynamicDto dto = new ConsumptionDynamicDto();
        dto.setColor(color);
        dto.setName(name);
        dto.setData(data);
        return dto;
    }

    public static EnergyCertificate getEnergyEfficiency(Float efficiencyLevel) {
        EnergyCertificate energyEfficiency = EnergyCertificate.Others;
        if (efficiencyLevel >= 0 && efficiencyLevel < 25) {
            energyEfficiency = EnergyCertificate.APlus;
        }
        if (efficiencyLevel >= 26 && efficiencyLevel < 50) {
            energyEfficiency = EnergyCertificate.A;
        }
        if (efficiencyLevel >= 51 && efficiencyLevel < 75) {
            energyEfficiency = EnergyCertificate.B;
        }
        if (efficiencyLevel >= 76 && efficiencyLevel < 100) {
            energyEfficiency = EnergyCertificate.BMinus;
        }
        if (efficiencyLevel >= 101 && efficiencyLevel < 150) {
            energyEfficiency = EnergyCertificate.C;
        }
        if (efficiencyLevel >= 151 && efficiencyLevel < 200) {
            energyEfficiency = EnergyCertificate.D;
        }
        if (efficiencyLevel >= 201 && efficiencyLevel < 250) {
            energyEfficiency = EnergyCertificate.E;
        }
        if (efficiencyLevel >= 250) {
            energyEfficiency = EnergyCertificate.F;
        }
        return energyEfficiency;
    }

    public static void updateList(List<Float> standardAVals,
                                  List<Float> standardBVals,
                                  List<Float> standardCVals,
                                  List<Float> standardDVals,
                                  Float efficiencyLevel,
                                  EnergyCertificate efficiency,
                                  int month) {
        if (efficiency.equals(EnergyCertificate.A) || efficiency.equals(EnergyCertificate.APlus)) {
            standardAVals.add(month, efficiencyLevel);
            /*standardBVals.add(month, 0f);
            standardCVals.add(month, 0f);
            standardDVals.add(month, 0f);*/
        } else if (efficiency.equals(EnergyCertificate.B) || efficiency.equals(EnergyCertificate.BMinus)) {
            standardBVals.add(month, efficiencyLevel);
          /*  standardAVals.add(month, 0f);
            standardCVals.add(month, 0f);
            standardDVals.add(month, 0f);*/
        } else if (efficiency.equals(EnergyCertificate.C)) {
            /*standardAVals.add(month, 0f);
            standardBVals.add(month, 0f);*/
            standardCVals.add(month, efficiencyLevel);
            //standardDVals.add(month, 0f);
        } else if (efficiency.equals(EnergyCertificate.D)
                || efficiency.equals(EnergyCertificate.F)
                || efficiency.equals(EnergyCertificate.E)) {
            /*standardAVals.add(month, 0f);
            standardBVals.add(month, 0f);
            standardCVals.add(month, 0f);*/
            standardDVals.add(month, efficiencyLevel);
        }

    }


    public static EnergyCertificate getNationalMedianCert() {
        //TODO read this values from national database
        float eeAPlus = 7021f;
        float eeA = 15839f;
        float eeB = 20887f;
        float eeBMinus = 49069f;
        float eeC = 229994f;
        float eeD = 249806f;
        float eeE = 152569f;
        float eeF = 83306f;
        return getEnergyEfficiency((eeAPlus + eeA + eeB + eeBMinus + eeC + eeD + eeE + eeF) / 8);
    }

    public static EnergyCertificate getPropertyTargetCert(EnergyCertificate baseLineEE) {
        switch (baseLineEE) {
            case A:
                return EnergyCertificate.APlus;
            case B:
                return EnergyCertificate.A;
            case BMinus:
                return EnergyCertificate.B;
            case C:
                return EnergyCertificate.BMinus;
            case D:
                return EnergyCertificate.C;
            case E:
                return EnergyCertificate.D;
            case F:
                return EnergyCertificate.E;
        }
        return EnergyCertificate.Others;
    }

    @AllArgsConstructor
    @Getter
    private static class CertificateInfo {
        int rank;
        Float value;
        EnergyCertificate certificateType;
    }
}
