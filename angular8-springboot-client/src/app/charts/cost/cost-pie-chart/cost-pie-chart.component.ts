import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';

@Component({
  selector: 'app-cost-pie-chart',
  templateUrl: './cost-pie-chart.component.html',
  styleUrls: ['./cost-pie-chart.component.css']
})
export class CostPieChartComponent implements OnInit {
  highcharts = Highcharts;
  chartOptions = {
    chart: {
      plotBorderWidth: null,
      plotShadow: false,
      backgroundColor: null
    },
    title: {
      text: 'Average Tariff Cost Structure'
    },
    tooltip: {
      pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
    },
    plotOptions: {
      pie: {
        allowPointSelect: true,
        cursor: 'pointer',

        dataLabels: {
          enabled: false
        },

        showInLegend: true
      }
    },
    series: [{
      type: 'pie',

      data: [
        {
          name: 'Contracted\n' +
            'Power',
          y: 3.2,
          color: '#0066cc'
        },
        {
          name: 'Off-hours',
          y: 6.1,
          color: '#248f24'
        },
        {
          name: 'Free-hours',
          y: 10.8,
          color: '#ffff00'
        },
        /* {
           name: 'Chrome',
           y: 12.8,
           sliced: true,
           selected: true
         },*/
        {
          name: 'Free-hours',
          y: 10.8,
          color: '#ffff00'
        },
        {
          name: 'Peak-hours',
          y: 17.3,
          color: '#ff0000'
        },
        {
          name: 'Normal-hours',
          y: 43.6,
          color: '#ff944d'
        },
        {
          name: 'Power in Peak Hours',
          y: 18.08,
          color: '#ff6666'
        },
        {
          name: 'Reactive Power',
          y: 0.2,
          color: '#ff00ff'
        },
      ]
    }]
  };

  constructor() {
  }

  ngOnInit() {
  }

}
