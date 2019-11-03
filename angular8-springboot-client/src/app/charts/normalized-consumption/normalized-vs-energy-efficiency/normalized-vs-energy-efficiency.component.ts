import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';

@Component({
  selector: 'app-normalized-vs-energy-efficiency',
  templateUrl: './normalized-vs-energy-efficiency.component.html',
  styleUrls: ['./normalized-vs-energy-efficiency.component.css']
})
export class NormalizedVsEnergyEfficiencyComponent implements OnInit {
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
      text: ''
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
      gridLineWidth: 0,
      plotBands: [
        {
          color: '#1fc105',
          from: 0,
          to: 8
        },
        {
          color: '#ffff33',
          from: 8,
          to: 9.6
        },
        {
          color: '#fc8a09',
          from: 9.6,
          to: 11.3
        },
        {
          color: '#ff4817',
          from: 11.3,
          to: 13
        }
      ],
      title: {
        text: 'kWh/m2'
      },
      labels: {
        style: {
          fontSize: '15px'
        }
      },
      // gridLineColor: '#cccccc',
      // gridLineWidth: 2,
      // tickInterval: 0.25
    },
    legend: {
      reversed: false
    },
    tooltip: {
      valueSuffix: 'kWh/m2'
    },
    plotOptions: {
      column: {
        stacking: 'normal'
      }
    },
    series: [
      {
        name: 'Standard A',
        data: [7.5, 7.5, 7.5, 7.5, 7.5, 7.5, 7.5, 7.5, 7.5, 7.5, 7.5, 7.5],
        color: '#1fc105'
      },
      {
        name: 'Standard B',
        data: [9.17, 9.17, 9.17, 9.17, 9.17, 9.17, 9.17, 9.17, 9.17, 9.17, 9.17, 9.17],
        color: '#ffff33'
      },
      {
        name: 'Standard C',
        data: [10.83, 10.83, 10.83, 10.83, 10.83, 10.83, 10.83, 10.83, 10.83, 10.83, 10.83, 10.83],
        color: '#fc8a09'
      },
      {
        name: 'Standard D',
        data: [12.5, 12.5, 12.5, 12.5, 12.5, 12.5, 12.5, 12.5, 12.5, 12.5, 12.5, 12.5],
        color: '#ff4817'
      },
      {
        name: 'Total',
        data: [10.47, 11.03, 9.663, 9.943, 8.007, 7.838, 8.626, 9.378, 9.028, 9.258, 9.496, 11.06],
        color: '#0b0003'
      }
    ]
  };

  constructor() {
  }

  ngOnInit() {
  }

}
