import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';
import {first} from 'rxjs/operators';
import {ChartService} from '../../chartService';
import {Router} from '@angular/router';

@Component({
  selector: 'app-cost-stack-chart',
  templateUrl: './cost-stack-chart.component.html',
  styleUrls: ['./cost-stack-chart.component.css']
})
export class CostStackChartComponent implements OnInit {

  highcharts = Highcharts;
  loading = true;
  chartOptions: any;

  constructor(private chartService: ChartService,
              private router: Router) {
  }

  ngOnInit() {
    this.chartService.costStackData()
      .pipe(first())
      .subscribe(
        data => {
          this.highcharts = Highcharts;
          this.chartOptions = {
            chart: {
              type: 'column',
              /*     backgroundColor: null, */
              /* grid: true, */
              /*    gridLineColor: '#0066cc', */
              /* plotBackgroundColor: '#F7F7F7', */
              /*    width: 800,
                 height: 400 */
            },
            title: {
              text: 'Monthly electricity cost'
            },
            xAxis: {
              // TODO check errors
              categories: data.content.xvalues,
              // categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sept', 'Oct', 'Nov', 'Dec']
            },
            yAxis: {
              min: 0,
              // max: 5000,
              title: {
                text: 'Euro',
              },
              labels: {
                overflow: 'justify'
              },
              gridLineColor: '#cccccc',
              gridLineWidth: 2
            },
            tooltip: {
              // valueSuffix: ' millions',
              // pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> ({point.percentage:.0f}%)<br/>',
              pointFormat: '{series.name}: <b>{point.y:.2f}</b><br/>',
              // shared: true
            }
            ,
            plotOptions: {
              column: {
                stacking: 'normal',
                pointPadding: 0.4,
                // borderWidth: 0,
                groupPadding: 0,
              }
            }
            ,

            series: [
              {
                name: 'Contracted Power' +
                  'Power',
                data: data.content.contractedPowerValues,
                color: '#3A9AFC'
              },
              {
                name: 'Power in Peak Hours',
                data: data.content.powerInPeakValues,
                color: '#0B2161'
              },
              {
                name: 'Reactive\nPower',
                data: data.content.reactivePowerValues,
                color: '#6554C0'
              },
              {
                name: 'Normal-hours',
                data: data.content.normalValues,
                color: '#FFAB00'
              },
              {
                name: 'Peak-hours',
                data: data.content.peakValues,
                color: '#FF5630'
              },
              {
                name: 'Free hours',
                data: data.content.freeValues,
                color: '#55E27C'
              },
              {
                name: 'Off hours',
                data: data.content.offValues,
                color: '#219653'
              }
            ]
            ,
            credits: {
              enabled: false
            }
          };
          this.loading = false;
        },
        () => {
        });
  }
}
