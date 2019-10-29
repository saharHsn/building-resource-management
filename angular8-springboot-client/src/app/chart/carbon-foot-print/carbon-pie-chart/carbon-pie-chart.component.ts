import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';

@Component({
  selector: 'app-carbon-pie-chart',
  templateUrl: './carbon-pie-chart.component.html',
  styleUrls: ['./carbon-pie-chart.component.css']
})
export class CarbonPieChartComponent implements OnInit {
  highcharts = Highcharts;
  chartOptions = {
    chart: {
      plotBorderWidth: null,
      plotShadow: false,
      backgroundColor: null
    },
    title: {
      text: 'Average Contribution of Tariffs in Carbon Footprint'
    },
    tooltip: {
      pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
    },
    plotOptions: {
      pie: {
        allowPointSelect: true,
        cursor: 'pointer',

        dataLabels: {
          enabled: true,
          format: '<br>{point.percentage:.1f} %',
          distance: -50,
          filter: {
            property: 'percentage',
            operator: '>',
            value: 4
          }
        },

        showInLegend: true
      }
    },
    /*legend: {
      layout: 'vertical',
      align: 'right',
      verticalAlign: 'middle',
      floating: false,
      itemStyle: {
        font: '9pt Trebuchet MS, Verdana, sans-serif',
        color: 'rgba(26,30,27,0.92)'
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
    },*/
    series: [{
      type: 'pie',
      data: [
        {
          name: 'CO2 Off-hours',
          y: 11.4,
          color: '#0099ff'
        },
        {
          name: 'CO2 Free-hours',
          y: 19.4,
          color: '#ffff00'
        },
        {
          name: 'CO2 Peak-hours',
          y: 18.1,
          color: '#ff0000'
        },
        {
          name: 'CO2 Normal-hours',
          y: 51,
          color: '#ff944d'
        }
      ]
    }]
  };

  constructor() {
  }

  ngOnInit() {
  }

}
