import {Component, Input, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';

@Component({
  selector: 'app-consumption-dynamic-bar-chart',
  templateUrl: './consumption-dynamic-bar-chart.component.html',
  styleUrls: ['./consumption-dynamic-bar-chart.component.css']
})
export class ConsumptionDynamicBarChartComponent implements OnInit {
  title = 'app';
  chart;
  Highcharts = Highcharts;

  constructor() {
  }

  // tslint:disable-next-line:max-line-length
  xAxisCategories = ['Jan-2017', 'Feb-2017', 'Mar-2017', 'Apr-2017', 'May-2017', 'Jun-2017', 'Jul-2017', 'Aug-2017', 'Sept-2017', 'Oct-2017', 'Nov-2017', 'Dec-2017'];
  dataSeries = [{
    name: 'Power in Peak Hours',
    data: [833.69, 846.81, 739.59, 896.43, 684.86, 672.34, 742.91, 782.83, 753.81, 762.87, 714.73, 786.84],
    color: '#ff6666'
  }];
  highcharts = Highcharts;
  @Input()
  chartOptions;
  defaultOptions = {
    chart: {
      events: {
        load() {
        },
      },
      type: 'column',
      backgroundColor: null,
      grid: true,
      gridLineColor: '#0066cc',
      plotBackgroundColor: '#F7F7F7',
      width: 800,
      height: 400
    },
    title: {
      // text: 'Cost of Electricity Over Time'
      text: ''
    },
    xAxis: {
      categories: this.xAxisCategories,
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
      pointFormat: '{series.name}: <b>{point.y:.2f}</b><br/>',
    },
    plotOptions: {
      column: {
        stacking: 'normal'
      }
    },
    series: this.dataSeries,
    credits: {
      enabled: false
    }
  };

  ngOnInit() {
    console.log(this.chartOptions);
  }
}
