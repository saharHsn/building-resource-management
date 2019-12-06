import {Component, Input, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';
import {ChartService} from '../../../chartService';
import {Router} from '@angular/router';
import {first} from 'rxjs/operators';
import {YearFilterType} from '../filter-form/enum/YearFilterType';
import {TimePeriodType} from '../filter-form/enum/TimePeriodType';
import {DatePartType} from '../filter-form/enum/DatePartType';

@Component({
  selector: 'app-consumption-dynamic-bar-chart',
  templateUrl: './consumption-dynamic-bar-chart.component.html',
  styleUrls: ['./consumption-dynamic-bar-chart.component.css']
})
export class ConsumptionDynamicBarChartComponent implements OnInit {
  title = 'app';
  chart;
  buildingId: string;
  highcharts = Highcharts;
  @Input()
  chartOptions: any;
  xAxisCategories: any;
  dataSeries: any;
  defaultOptions: any;

  constructor(private chartService: ChartService,
              private router: Router) {
  }

  ngOnInit() {
    this.chartService.consumptionDynamicData(this.buildingId, YearFilterType.YEAR_2019, TimePeriodType.MONTHLY, DatePartType.PEAK_HOURS)
      .pipe(first())
      .subscribe(
        data => {
          // tslint:disable-next-line:max-line-length
          this.xAxisCategories = ['Jan-2017', 'Feb-2017', 'Mar-2017', 'Apr-2017', 'May-2017', 'Jun-2017', 'Jul-2017', 'Aug-2017', 'Sept-2017', 'Oct-2017', 'Nov-2017', 'Dec-2017'];
          this.dataSeries = [{
            name: data.content.name,
            data: data.content.data,
            color: data.content.color
          }];
          this.highcharts = Highcharts;
          /*@Input()
           this.chartOptions;*/
          this.defaultOptions = {
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
              categories: this.xAxisCategories,
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
            series: this.dataSeries,
            credits: {
              enabled: false
            }
          };
        },
        () => {
        });
  }
}
