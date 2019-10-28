import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';

@Component({
  selector: 'app-cost-stack-chart',
  templateUrl: './cost-stack-chart.component.html',
  styleUrls: ['./cost-stack-chart.component.css']
})
export class CostStackChartComponent implements OnInit {
  highcharts = Highcharts;
  chartOptions = {
    chart: {
      type: 'column',
      backgroundColor: null,
      grid: true,
      gridLineColor: '#0066cc',
      plotBackgroundColor: '#F7F7F7',
      width: 600,
      height: 400
    },
    title: {
      text: 'Cost of Electricity Over Time'
    },
    xAxis: {
      categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sept', 'Oct', 'Nov', 'Dec'],
    },
    legend: {
      layout: 'vertical',
      align: 'right',
      verticalAlign: 'middle',
      floating: false,
      itemStyle: {
        font: '9pt Trebuchet MS, Verdana, sans-serif',
        color: '#A0A0A0'
      },
      itemHoverStyle: {
        color: '#FFF'
      },
      itemHiddenStyle: {
        color: '#444'
      },
      symbolHeight: 12,
      symbolWidth: 12,
      y: -20
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
        data: [125.79, 131.4, 118.36, 131.4, 126.81, 131.04, 126.81, 131.04, 131.04, 131.99, 136.69, 131.99],
        color: '#0066cc'
      },
      {
        name: 'Power in\nPeak Hours',
        data: [833.69, 846.81, 739.59, 896.43, 684.86, 672.34, 742.91, 782.83, 753.81, 762.87, 714.73, 786.84],
        color: '#ff6666'
      },
      {
        name: 'Reactive\nPower',
        data: [9.34, 9.32, 10.21, 10.56, 13.36, 16.35, 9.78, 6.43, 6.4, 9.37, 12.25, 6.02],
        color: '#ff00ff'
      },
      {
        name: 'Normal\nHours',
        data: [1844.74, 1932.34, 1715.54, 1833.96, 1538.25, 1451.76, 1729.1, 878.35, 1743.17, 1881.22, 1728.28, 1834.11],
        color: '#ff944d'
      },
      {
        name: 'Peak\nHours',
        data: [878.11, 944.15, 829.95, 690.54, 473.42, 429.33, 513.55, 547.49, 481.35, 527.35, 695.46, 865.32],
        color: '#ff0000'
      },
      {
        name: 'Free\nHours',
        data: [437.62, 402.36, 467.34, 460.2, 369.59, 414.66, 368.28, 406.59, 453.63, 397.01, 427.34, 556.36],
        color: '#ffff00'
      },
      {
        name: 'Off\nHours',
        data: [265.96, 282.73, 238.51, 250.71, 211.15, 209.45, 209, 228.47, 227.03, 220.12, 245.33, 310.36],
        color: '#248f24'
      }
    ]
    ,
    credits: {
      enabled: false
    }
  };

  constructor() {
  }

  ngOnInit() {
  }
}
