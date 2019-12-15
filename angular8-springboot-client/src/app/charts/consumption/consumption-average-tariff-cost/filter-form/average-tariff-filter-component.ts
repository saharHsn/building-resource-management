import {Component, EventEmitter, Output} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {AverageTariffFilter} from './average-tariff-filter';
import {YearFilterType} from './enum/YearFilterType';
import {DatePartType} from './enum/DatePartType';
import {TimePeriodType} from './enum/TimePeriodType';
import {first} from 'rxjs/operators';
import {ChartService} from '../../../chartService';
import {Router} from '@angular/router';
import {ConsumptionDynamic} from '../ConsumptionDynamic';

@Component({
  selector: 'app-average-tariff-filter',
  templateUrl: './average-tariff-filter-component.html'
})

export class AverageTariffFilterComponent {
  tariffFilter: AverageTariffFilter;

  constructor(public fb: FormBuilder,
              private chartService: ChartService,
              private router: Router) {
    this.tariffFilter = new AverageTariffFilter();
    this.tariffFilter.year = YearFilterType.YEAR_2019;
    this.tariffFilter.datePartType = DatePartType.FREE_HOURS;
    this.tariffFilter.periodType = TimePeriodType.MONTHLY;
  }

  datePartType: DatePartType;
  year: YearFilterType;
  timePeriod: TimePeriodType;


  changedDataSeries;
  changedXAxisCategories;
  @Output() chartOptions = new EventEmitter();


  setDataPartType($event, year: YearFilterType, timePeriod: TimePeriodType) {
    this.datePartType = $event;
    this.redraw(year, this.datePartType, timePeriod);
  }

  setYear($event, timePeriod: TimePeriodType, datePartType: DatePartType) {
    this.year = $event;
    this.redraw(this.year, datePartType, timePeriod);
  }

  setTimePeriod($event, year: YearFilterType, datePartType: DatePartType) {
    this.timePeriod = $event;
    this.redraw(year, datePartType, this.timePeriod);
  }

  private redraw(year: YearFilterType, datePartType: DatePartType, timePeriod: TimePeriodType) {
    this.chartService.consumptionDynamicData(YearFilterType[year], timePeriod, datePartType)
      .pipe(first())
      .subscribe(
        data => {
          this.extracted(data, year, this.timePeriod);
        },
        () => {
        });
  }

  private extracted(data, year: YearFilterType, timePeriod: TimePeriodType) {
    const consumption = new ConsumptionDynamic();
    const content = data.content;
    consumption.color = content.color;
    consumption.data = content.data;
    consumption.name = content.name;
    this.changedDataSeries = consumption;
    this.changedXAxisCategories = this.getChangedXAxisCategories(year, timePeriod);
    this.changedDataSeries = consumption;
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
}
