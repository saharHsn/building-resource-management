import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';

@Component({
  selector: 'app-color-energy',
  templateUrl: './color-energy.component.html',
  styleUrls: ['./color-energy.component.css']
})
export class ColorEnergyComponent implements OnInit {
  Highcharts = Highcharts;
  chartOptions = {};

  constructor() {
  }

  ngOnInit() {
    this.chartOptions = {
      chart: {
        inverted: true,
        polar: false,
        plotBorderWidth: null,
        plotShadow: false,
        backgroundColor: null,
      },
      title: {
        text: ''
      },
      plotOptions: {
        series: {
          groupPadding: 0,
          stacking: 'normal',
          pointWidth: 40
        }
      },

      subtitle: {
        text: ''
      },

      xAxis: {
        categories: ['A+', ' A', ' B', 'B-', ' C', ' D', ' E', ' F']
      }, yAxis: {
        min: 0,
        gridLineWidth: 0,
        title: {
          text: '',
          align: 'high'
        },
        labels: {
          enabled: false// default is true
        }
      },
      credits: {
        enabled: false
      },

      series: [{
        colors: ['#059321', '#55E27C', '#E8FC6B', '#EFFF1E', '#FADB00', '#FF8300', '#FF4800',
          '#F90000'],
        type: 'column',
        colorByPoint: true,
        data: [1, 2, 3, 4, 5, 6, 7, 8],
        showInLegend: false
      }]
    };
  }

}
