import {YearFilterType} from './enum/YearFilterType';
import {TimePeriodType} from './enum/TimePeriodType';
import {DatePartType} from './enum/DatePartType';

export class AverageTariffFilter {
  id: string;
  name: string;
  year: YearFilterType;
  periodType: TimePeriodType;
  datePartType: DatePartType;
}
