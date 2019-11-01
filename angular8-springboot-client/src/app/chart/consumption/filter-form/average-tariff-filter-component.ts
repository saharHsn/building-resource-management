import {Component, EventEmitter, Output} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {AverageTariffFilter} from './average-tariff-filter';
import {YearFilterType} from './enum/YearFilterType';
import {DatePartType} from './enum/DatePartType';
import {TimePeriodType} from './enum/TimePeriodType';

@Component({
  selector: 'app-average-tariff-filter',
  templateUrl: './average-tariff-filter-component.html'
})

export class AverageTariffFilterComponent {
  tariffFilter: AverageTariffFilter;

  constructor(public fb: FormBuilder) {
    this.tariffFilter = new AverageTariffFilter();
    this.tariffFilter.year = YearFilterType.YEAR_2019;
    this.tariffFilter.datePartType = DatePartType.FREE_HOURS;
    this.tariffFilter.periodType = TimePeriodType.MONTHLY;
  }

  dataPartType: DatePartType;
  year: YearFilterType;
  timePeriod: TimePeriodType;

  changedDataSeries;
  changedXAxisCategories;
  @Output() chartOptions = new EventEmitter();

  setDataPartType($event) {
    this.dataPartType = $event;
    this.redrawChart(this.dataPartType, this.year, this.timePeriod);
  }

  setYear($event) {
    this.year = $event;
    this.redrawChart(this.dataPartType, this.year, this.timePeriod);
  }

  setTimePeriod($event) {
    this.timePeriod = $event;
    this.redrawChart(this.dataPartType, this.year, this.timePeriod);
  }

  private redrawChart(dataPartType: DatePartType,
                      year: YearFilterType,
                      timePeriod: TimePeriodType,
  ) {

    this.changedXAxisCategories = this.getChangedXAxisCategories(year, timePeriod);
    this.changedDataSeries = this.getChangedDataSeries(dataPartType);
    this.chartOptions.emit({
      chart: {
        events: {
          load() {
          },
        },
        type: 'column',
        backgroundColor: null,
        grid: true,
        gridLineColor: '#0066cc',
        plotBackgroundColor: '#F7F7F7',
        width: 800,
        height: 400
      },
      title: {
        // text: 'Cost of Electricity Over Time'
        text: ''
      },
      xAxis: {
        categories: this.changedXAxisCategories,
      },
      yAxis: {
        min: 0,
        // max: 5000,
        title: {
          text: 'Euros',
        },
        labels: {
          overflow: 'justify'
        },
        gridLineColor: '#cccccc',
        gridLineWidth: 2
      },
      tooltip: {
        pointFormat: '{series.name}: <b>{point.y:.2f}</b><br/>',
      },
      plotOptions: {
        column: {
          stacking: 'normal'
        }
      },
      series: this.changedDataSeries,
      credits: {
        enabled: false
      }
    });
  }

  private getChangedXAxisCategories(year: YearFilterType, timePeriod: TimePeriodType) {
    let xAxisCategories = this.getXAxisCategories(year ? year : YearFilterType.YEAR_2017);
    if (timePeriod && TimePeriodType[timePeriod] === TimePeriodType.QUARTERS) {
      xAxisCategories = this.getXAxisQuarters(year ? year : YearFilterType.YEAR_2017);
    }
    return xAxisCategories;
  }

  private getXAxisQuarters(year: YearFilterType) {
    const yearValue = year.valueOf();
    return ['Q1 ' + yearValue, 'Q2 ' + yearValue, 'Q3 ' + yearValue, 'Q4 ' + yearValue];
  }

  private getXAxisCategories(year: YearFilterType) {
    const yearValue = year.valueOf();
    return ['Jan-' + yearValue, 'Feb-' + yearValue, 'Mar-' + yearValue, 'Apr-' + yearValue, 'May-' + yearValue,
      'Jun-' + yearValue, 'Jul-' + yearValue, 'Aug-' + yearValue, 'Sept-' + yearValue, 'Oct-' + yearValue, 'Nov-' + yearValue,
      'Dec-' + yearValue];
  }

  private getChangedDataSeries(datePartType: DatePartType) {
    const peakHours = [{
      name: 'Peak Hours',
      data: [878.11, 944.15, 829.95, 690.54, 473.42, 429.33, 513.55, 547.49, 481.35, 527.35, 695.46, 865.32],
      color: '#ff0000'
    }];
    const normalHours = [{
      name: 'Normal Hour',
      data: [125.79, 131.4, 118.36, 131.4, 126.81, 131.04, 126.81, 131.04, 131.04, 131.99, 136.69, 131.99],
      color: '#0066cc'
    }];
    const freeHours = [{
      name: 'Free Hours',
      data: [9.34, 9.32, 10.21, 10.56, 13.36, 16.35, 9.78, 6.43, 6.4, 9.37, 12.25, 6.02],
      color: '#ffff00'
    }];
    const offHours = [{
      name: 'Off Hours',
      data: [265.96, 282.73, 238.51, 250.71, 211.15, 209.45, 209, 228.47, 227.03, 220.12, 245.33, 310.36],
      color: '#248f24'
    }];

    const peakHoursQ = [{
      name: 'Peak Hours',
      data: [878.11, 944.15, 829.95, 690.54],
      color: '#ff0000'
    }];
    const normalHoursQ = [{
      name: 'Normal Hour',
      data: [125.79, 131.4, 118.36, 131.4],
      color: '#0066cc'
    }];
    const freeHoursQ = [{
      name: 'Free Hours',
      data: [9.34, 9.32, 10.21, 10.56],
      color: '#ffff00'
    }];
    const offHoursQ = [{
      name: 'Off Hours',
      data: [265.96, 282.73, 238.51, 250.71],
      color: '#248f24'
    }];
    const isQuarter = this.timePeriod && TimePeriodType[this.timePeriod] === TimePeriodType.QUARTERS;

    if (!datePartType || DatePartType[datePartType] === DatePartType.FREE_HOURS) {
      return (isQuarter ? freeHoursQ : freeHours);
    } else if (DatePartType[datePartType] === DatePartType.NORMAL_HOURS) {
      return (isQuarter ? normalHoursQ : normalHours);
    } else if (DatePartType[datePartType] === DatePartType.OFF_HOURS) {
      return (isQuarter ? offHoursQ : offHours);
    } else if (DatePartType[datePartType] === DatePartType.PEAK_HOURS) {
      return (isQuarter ? peakHoursQ : peakHours);
    }
  }
}
