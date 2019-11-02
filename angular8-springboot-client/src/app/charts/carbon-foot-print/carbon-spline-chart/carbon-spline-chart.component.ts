import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';

@Component({
  selector: 'app-carbon-spline-chart',
  templateUrl: './carbon-spline-chart.component.html',
  styleUrls: ['./carbon-spline-chart.component.css']
})
export class CarbonSplineChartComponent implements OnInit {
  highcharts = Highcharts;
  chartOptions = {
    chart: {
      type: 'spline',
      backgroundColor: null,
      width: 800,
      height: 500,
      grid: true,
      gridLineColor: '#0066cc'
    },
    title: {
      text: 'Normalized Carbon Footprint vs National Baseline'
    },
    xAxis: {
      categories: ['Jan-2018',
        'Feb-2018',
        'Mar-2018',
        'Apr-2018',
        'May-2018',
        'Jun-2018',
        'Jul-2018',
        'Aug-2018',
        'Sep-2018',
        'Oct-2018',
        'Nov-2018',
        'Dec-2018'],
      labels: {
        style: {
          fontSize: '15px'
        }
      }
    },
    yAxis: {
      title: {
        text: 'KG-CO2'
      },
      labels: {
        style: {
          fontSize: '15px'
        }
      },
      gridLineColor: '#cccccc',
      gridLineWidth: 2,
      tickInterval: 0.25
    },
    tooltip: {
      valueSuffix: 'KG-CO2'
    },
    series: [{
      name: 'Total',
      data: [4.28, 4.51, 3.95, 4.07, 3.27, 3.2, 3.53, 3.83, 3.69, 3.78, 3.88, 4.52],
      color: '#0b0003'
    },
      {
        name: 'Baseline',
        data: [3.74, 3.74, 3.74, 3.74, 3.74, 3.74, 3.74, 3.74, 3.74, 3.74, 3.74, 3.74],
        color: '#f60b4e'
      }
    ]
  };

  constructor() {
  }

  ngOnInit() {
  }

}
