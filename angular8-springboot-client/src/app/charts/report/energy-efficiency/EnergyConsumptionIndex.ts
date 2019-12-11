import {EnergyCertificate} from '../../../building/enums/energyCertificate';

export class EnergyConsumptionIndex {
  baseline: number;
  thisMonth: number;
  propertiesTarget: number;
  nationalMedian: number;
  baseLineCert: EnergyCertificate;
  thisMonthCert: EnergyCertificate;
  propertyTargetCert: EnergyCertificate;
  nationalMedianCert: EnergyCertificate;
}
