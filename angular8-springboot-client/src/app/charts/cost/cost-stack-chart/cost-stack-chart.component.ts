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
  buildingId: string;
  highcharts = Highcharts;
  loading = true;
  chartOptions: any;

  constructor(private chartService: ChartService,
              private router: Router) {
  }

  ngOnInit() {
    this.chartService.costStackData(this.buildingId)
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
              text: 'Cost of Electricity Over Time'
            },
            xAxis: {
              // TODO check errors
              categories: data.content.xValues,
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
                stacking: 'normal'
              }
            }
            ,

            series: [
              {
                name: 'Contracted\n' +
                  'Power',
                data: data.content.contractedPowerValues,
                color: '#0066cc'
              },
              {
                name: 'Power in\nPeak Hours',
                data: data.content.powerInPeakValues,
                color: '#ff6666'
              },
              {
                name: 'Reactive\nPower',
                data: data.content.reactivePowerValues,
                color: '#ff00ff'
              },
              {
                name: 'Normal\nHours',
                data: data.content.normalValues,
                color: '#ff944d'
              },
              {
                name: 'Peak\nHours',
                data: data.content.peakValues,
                color: '#ff0000'
              },
              {
                name: 'Free\nHours',
                data: data.content.freeValues,
                color: '#ffff00'
              },
              {
                name: 'Off\nHours',
                data: data.content.offValues,
                color: '#248f24'
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
