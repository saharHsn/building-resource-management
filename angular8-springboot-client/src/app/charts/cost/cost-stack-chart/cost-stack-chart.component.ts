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
              text: 'Cost of Electricity Over Time'
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
                text: 'Euros',
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
                data: [50.94, 50.94, null, null, null, null, null, null, null, null, null], /*  data.content.contractedPowerValues, */
                color: '#3A9AFC'
              },
              {
                name: 'Power in Peak Hours',
                data: [64.48, 64.48, null, null, null, null, null, null, null, null, null], /*  data.content.powerInPeakValues, */
                color: '#6554C0'
              },
              {
                name: 'Reactive\nPower',
                data: [64.48, 64.48, null, null, null, null, null, null, null, null, null], /* data.content.reactivePowerValues, */
                color: '#FF5630'
              },
              {
                name: 'Normal-hours',
                data: [160.2, 160.2, null, null, null, null, null, null, null, null, null], /* data.content.normalValues, */
                color: '#FFAB00'
              },
              {
                name: 'Peak-hours',
                data: [160.2, 160.2, null, null, null, null, null, null, null, null, null], /*  data.content.peakValues, */
                color: '#36B37E'
              },
              {
                name: 'Free hours',
                data: [33.37, 33.37, null, null, null, null, null, null, null, null, null], /*  data.content.freeValues, */
                color: '#00B8D9'
              },
              {
                name: 'Off hours',
                data: [25.89, 25.89, null, null, null, null, null, null, null, null, null],
                color: '#0065FF'
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
