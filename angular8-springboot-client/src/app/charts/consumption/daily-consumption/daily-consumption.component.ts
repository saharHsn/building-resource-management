import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';
import {first} from 'rxjs/operators';
import {ChartService} from '../../chartService';

interface Select {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-daily-consumption',
  templateUrl: './daily-consumption.component.html',
  styleUrls: ['./daily-consumption.component.css']
})
export class DailyConsumptionComponent implements OnInit {
  highcharts = Highcharts;
  chartOptions: any;
  loading = true;


  year: number;
  month: number;

  // options of data
  months: Select[] = [
    {value: '1', viewValue: 'January'},
    {value: '2', viewValue: 'February'},
    {value: '3', viewValue: 'March'},
    {value: '4', viewValue: 'April'},
    {value: '5', viewValue: 'May'},
    {value: '6', viewValue: 'June'},
    {value: '7', viewValue: 'July'},
    {value: '8', viewValue: 'August'},
    {value: '9', viewValue: 'September'},
    {value: '10', viewValue: 'October'},
    {value: '11', viewValue: 'November'},
    {value: '12', viewValue: 'December'},
  ];
  years: Select[] = [
    {value: '2019', viewValue: '2019'},
    {value: '2020', viewValue: '2020'},

  ];

  // data for chart
  yearChart: any;
  monthChart: any;

  constructor(private _chartService: ChartService) {
    this.yearChart = String(new Date().getFullYear().toString());  /* new Date().getFullYear().toString(); */
    this.monthChart = String(new Date().getMonth());

  }

  ngOnInit() {
    this.initChart(this.monthChart, this.yearChart);
  }


  create() {
    this.initChart(this.monthChart, this.yearChart);
  }

  initChart(month, year) {

    this._chartService.gethistoricalConsumption(month, year).pipe(first()).subscribe(
      data => {
        this.highcharts = Highcharts;
        this.chartOptions = {
          chart: {
            type: 'column',

          },
          title: {
            text: 'Daily Electricity Consumption'
          },
          credits: {
            enabled: false
          },
          xAxis: {
            categories: data.content.xvalues,
          },
          yAxis: {
            min: 0,
            title: {
              text: 'kWh'
            },
            stackLabels: {
              enabled: false,
              style: {
                fontWeight: 'bold',
                color: ( // theme
                  Highcharts.defaultOptions.title.style &&
                  Highcharts.defaultOptions.title.style.color
                ) || 'gray'
              }
            }
          },
          legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'bottom'
          },
          tooltip: {
            headerFormat: '<b>{point.x}</b><br/>',
            pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
          },

          exporting: {
            enabled: true
          },


          plotOptions: {
            column: {
              stacking: 'normal',
              dataLabels: {
                enabled: false
              }
            }
          },
        
          series: [{
            name: 'Off hours',
            data: data.content.offValues,
            color: '#219653' //dark green
          }, {
            name: 'Free hours',
            data: data.content.freeValues,
            color: '#55E27C'
          }, {
            name: 'Normal-hours',
            data: data.content.normalValues,
            color: '#FFAB00'
          }, {
            name: 'Peak-hours',
            data: data.content.peakValues,
            color: ' #FF5630'
          }
          ]
        };
      },
      () => {
      });
  }

}


