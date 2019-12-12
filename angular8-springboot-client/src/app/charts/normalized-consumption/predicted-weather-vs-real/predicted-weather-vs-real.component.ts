import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';
import {ChartService} from '../../chartService';
import {Router} from '@angular/router';
import {first} from 'rxjs/operators';

@Component({
  selector: 'app-predicted-weather-vs-real',
  templateUrl: './predicted-weather-vs-real.component.html',
  styleUrls: ['./predicted-weather-vs-real.component.css']
})
export class PredictedWeatherVsRealComponent implements OnInit {
  buildingId: string;
  highcharts = Highcharts;
  chartOptions: any;


  constructor(private chartService: ChartService,
              private router: Router) {
  }

  ngOnInit() {
    this.chartService.predictedWeatherVSReal(this.buildingId)
      .pipe(first())
      .subscribe(
        data => {
          this.highcharts = Highcharts;
          this.chartOptions = {
            chart: {
              type: 'spline',
              backgroundColor: null,
              grid: true,
              width: 800,
              height: 500
            },
            title: {
              text: ''
            },
            xAxis: {
              // categories: ['Jan-2019', 'Feb-2019', 'Mar-2019'],
              categories: data.content.xvalues,
              labels: {
                style: {
                  fontSize: '15px'
                }
              }
            },
            yAxis: {
              title: {
                text: 'kWh/m2'
              },
              labels: {
                style: {
                  fontSize: '15px'
                }
              },
            },
            tooltip: {
              valueSuffix: 'kWh/m2'
            },
            series: [{
              name: 'Baseline',
              // data: [10.62, 9.85, 9.38],
              data: data.content.baseLineValues,
              color: '#0b0003'
            },
              {
                name: 'Consumption',
                // data: [10.94, 11.21, 9.3],
                data: data.content.consumptionValues,
                color: '#46f620'
              }
            ]
          };
        },
        () => {
        });
  }
}
