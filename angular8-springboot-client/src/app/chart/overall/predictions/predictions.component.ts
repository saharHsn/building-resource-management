import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';

@Component({
  selector: 'app-predictions',
  templateUrl: './predictions.component.html',
  styleUrls: ['./predictions.component.css']
})
export class PredictionsComponent implements OnInit {
  highcharts = Highcharts;
  chartOptions = {
    chart: {
      type: 'spline',
      backgroundColor: null,
      width: 330,
      height: 300,
      grid: true
    },
    title: {
      text: 'Predictions'
    },
    xAxis: {
      categories: ['Oct-2019', 'Nov-2019', 'Dec-2019'],
      labels: {
        style: {
          fontSize: '15px'
        }
      }
    },
    yAxis: {
      title: {
        text: '€'
      },
      labels: {
        style: {
          fontSize: '15px'
        }
      },
      categories: [0, 2500, 5000, 7500],
      tickInterval: 2500
    },
    tooltip: {
      valueSuffix: ' €'
    },
    series: [{
      name: 'Saving',
      data: [321, 420, 360]
    },
      {
        name: 'Cost',
        data: [6135.5, 7130.4, 6234.3]
      }
    ]
  };

  constructor() {
  }

  ngOnInit() {
  }

}
