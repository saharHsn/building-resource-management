import {TimePeriodType} from './filter-form/enum/TimePeriodType';
import {DatePartType} from './filter-form/enum/DatePartType';

export class ConsumptionDynamic {
  data: number[];
  periodType: TimePeriodType;
  year: number;
  datePartType: DatePartType;
  color: string;
  name: string;
}
