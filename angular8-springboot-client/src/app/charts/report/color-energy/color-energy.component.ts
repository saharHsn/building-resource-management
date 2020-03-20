import { Component, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts';
import HC_exporting from 'highcharts/modules/exporting';
@Component({
  selector: 'app-color-energy',
  templateUrl: './color-energy.component.html',
  styleUrls: ['./color-energy.component.css']
})
export class ColorEnergyComponent implements OnInit {
  Highcharts = Highcharts;
  chartOptions = {};
  constructor() { }

  ngOnInit() {
    this.chartOptions={
      chart: {
        inverted: true,
        polar: false
    },
title: {
    text: ''
},
plotOptions: {
    series: {
        groupPadding: 0
    }
},

subtitle: {
    text: ''
},

xAxis: {
    categories: ['A', 'B', 'C', 'D', 'E', 'F']
},yAxis: {
                min: 0,
                gridLineWidth: 0,
                title: {
                    text: '',
                    align: 'high'
                },
                labels:{
                    enabled:false//default is true
                }
            },
            credits: {
    enabled: false
},

series: [{
    colors: ['#55E27C', '#E8FC6B', '#FFF500', '#FFC700', '#FFC700',
    '#F90000'],
    type: 'column',
    colorByPoint: true,
    data: [1, 2, 3, 4, 5,6],
    showInLegend: false
}]
    }
  }

}
