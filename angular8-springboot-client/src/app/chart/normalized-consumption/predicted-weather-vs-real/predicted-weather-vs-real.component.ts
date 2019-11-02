import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';

@Component({
  selector: 'app-predicted-weather-vs-real',
  templateUrl: './predicted-weather-vs-real.component.html',
  styleUrls: ['./predicted-weather-vs-real.component.css']
})
export class PredictedWeatherVsRealComponent implements OnInit {
  highcharts = Highcharts;
  chartOptions = {
    chart: {
      type: 'spline',
      backgroundColor: null,
      grid: true,
      width: 800,
      height: 500
    },
    title: {
      text: 'Predicted Baseline by Weather vs Real Consumption'
    },
    xAxis: {
      categories: ['Jan-2019', 'Feb-2019', 'Mar-2019'],
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
      data: [10.62, 9.85, 9.38],
      color: '#0b0003'
    },
      {
        name: 'Consumption',
        data: [10.94, 11.21, 9.3],
        color: '#46f620'
      }
    ]
  };

  constructor() {
  }

  ngOnInit() {
  }

}
