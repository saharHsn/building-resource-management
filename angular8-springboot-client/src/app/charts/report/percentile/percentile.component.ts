import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';

@Component({
  selector: 'app-percentile',
  templateUrl: './percentile.component.html',
  styleUrls: ['./percentile.component.css']
})
export class PercentileComponent implements OnInit {
  Highcharts = Highcharts;
  chartOptions = {};
  constructor() {
  }

  ngOnInit() {
    this.chartOptions = {
      chart: {
        type: 'areaspline',
        plotBorderWidth: null,
        plotShadow: false,
        backgroundColor: null,
        height: 300,
        width: 300,
      },

      title: {
        text: 'Percentile'
    },
    legend: {
       /*  layout: 'vertical',
        align: 'left',
        verticalAlign: 'botton',
        x: 0,
        y: 0,
        floating: true,
        borderWidth: 1,
        backgroundColor: '#FFFFFF' */
      enabled: null
    },

    xAxis: {
      categories: [
        'Monday',
        'Tuesday',
        'Wednesday',
        'Thursday',
        'Friday',
        'Saturday',
        'Sunday'
      ],
      labels: {
        enabled: false// default is true
      }

    },
    yAxis: {
        title: {
            text: ''
        }
    },
    tooltip: {
        shared: true,
        valueSuffix: ' units'
    },
    credits: {
        enabled: false
    },
    plotOptions: {
        areaspline: {
            fillOpacity: 0.5
        }
    },
    series: [{
      name: 'Your score is higher than 63% of Builtrix METRIC members',
      data: [1, 3, 6, 10, 11, 10, null, 3, 1],
      color: '#3A9AFC'
    },
      {
        type: 'line',
        name: 'Your score is higher than 63% of Builtrix METRIC members',
        data: [null, null, null, null, null, 10, 6, 3, 1],
        color: '#3A9AFC'
      }]
    };


  }

}
