import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';
import {first} from 'rxjs/operators';
import {ChartService} from '../../chartService';
import {Router} from '@angular/router';

@Component({
  selector: 'app-electricity-consumption-over-time',
  templateUrl: './electricity-consumption-over-time.component.html',
  styleUrls: ['./electricity-consumption-over-time.component.css']
})
export class ElectricityConsumptionOverTimeComponent implements OnInit {

  highcharts = Highcharts;
  loading = true;
  chartOptions: any;

  constructor(private chartService: ChartService,
              private router: Router) {
  }

  ngOnInit() {
    this.chartService.consumptionStackData()
      .pipe(first())
      .subscribe(
        data => {
          this.highcharts = Highcharts;
          this.chartOptions = {
            chart: {
              type: 'column',
              backgroundColor: null,
              grid: true,
              gridLineColor: '#0066cc',
              plotBackgroundColor: '#F7F7F7',
              width: 800,
              height: 400
            },
            title: {
              text: ''
            },
            xAxis: {
              /* categories: ['Jan-2018', 'Feb-2018', 'Mar-2018', 'Apr-2018', 'May-2018', 'Jun-2018',
                 'Jul-2018', 'Aug-2018', 'Sept-2018', 'Oct-2018', 'Nov-2018', 'Dec-2018'],*/
              categories: data.content.xvalues,
            },

            yAxis: {
              min: 0,
              // max: 5000,
              title: {
                text: 'kWh',
              },
              labels: {
                overflow: 'justify'
              },
              gridLineColor: '#cccccc',
              gridLineWidth: 2
            },
            tooltip: {
              pointFormat: '{series.name}: <b>{point.y:.2f}</b><br/>',
            }
            ,
            plotOptions: {
              column: {
                stacking: 'normal'
              }
            }
            ,

            series: [
              {
                type: 'column',
                name: 'Contracted\n' +
                  'Power',
                // data: [125.79, 131.4, 118.36, 131.4, 126.81, 131.04, 126.81, 131.04, 131.04, 131.99, 136.69, 131.99],
                data: data.content.contractedPowerValues,
                color: '#0066cc'
              },
              {
                name: 'Power in\nPeak Hours',
                // data: [833.69, 846.81, 739.59, 896.43, 684.86, 672.34, 742.91, 782.83, 753.81, 762.87, 714.73, 786.84],
                data: data.content.powerInPeakValues,
                color: '#ff6666'
              },
              {
                name: 'Reactive\nPower',
                // data: [9.34, 9.32, 10.21, 10.56, 13.36, 16.35, 9.78, 6.43, 6.4, 9.37, 12.25, 6.02],
                data: data.content.reactivePowerValues,
                color: '#ff00ff'
              },
              {
                name: 'Normal\nHours',
                // data: [1844.74, 1932.34, 1715.54, 1833.96, 1538.25, 1451.76, 1729.1, 878.35, 1743.17, 1881.22, 1728.28, 1834.11],
                data: data.content.normalValues,
                color: '#ff944d'
              },
              {
                name: 'Peak\nHours',
                // data: [878.11, 944.15, 829.95, 690.54, 473.42, 429.33, 513.55, 547.49, 481.35, 527.35, 695.46, 865.32],
                data: data.content.peakValues,
                color: '#ff0000'
              },
              {
                name: 'Free\nHours',
                // data: [437.62, 402.36, 467.34, 460.2, 369.59, 414.66, 368.28, 406.59, 453.63, 397.01, 427.34, 556.36],
                data: data.content.freeValues,
                color: '#ffff00'
              },
              {
                name: 'Off\nHours',
                // data: [265.96, 282.73, 238.51, 250.71, 211.15, 209.45, 209, 228.47, 227.03, 220.12, 245.33, 310.36],
                data: data.content.offValues,
                color: '#248f24'
              },
              {
                type: 'spline',
                name: 'Baseline',
                // data: [1844.74, 1932.34, 1715.54, 1833.96, 1538.25, 1451.76, 1729.1, 878.35, 1743.17, 1881.22, 1728.28, 1834.11],
                data: data.content.baseLineValues,
                color: '#0b0003'
              }
            ]
            ,
            credits: {
              enabled: false
            }
          };
        },
        () => {
        });
  }

}
