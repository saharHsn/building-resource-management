import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';
import {first} from 'rxjs/operators';
import {ChartService} from '../../chartService';
import {Router} from '@angular/router';
import HC_exporting from 'highcharts/modules/exporting';
@Component({
  selector: 'app-daily-consumption',
  templateUrl: './daily-consumption.component.html',
  styleUrls: ['./daily-consumption.component.css']
})
export class DailyConsumptionComponent implements OnInit {
  highcharts = Highcharts;
  chartOptions: any;

  constructor() { }

  ngOnInit() {

    this.chartOptions={
      chart: {
        type: 'column',
        
    },
    title: {
        text: 'Daily Electricity Consumption'
    },
    credits: {
        enabled: false
    },
    xAxis: {
        categories: ['1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24','25','26','27','28','29','30','31']
    },
    yAxis: {
        min: 0,
        title: {
            text: 'kWh'
        },
        stackLabels: {
            enabled: false,
            style: {
                fontWeight: 'bold',
                color: ( // theme
                    Highcharts.defaultOptions.title.style &&
                    Highcharts.defaultOptions.title.style.color
                ) || 'gray'
            }
        }
    },
    legend: {
      layout: 'vertical',
      align: 'right',
      verticalAlign: 'bottom'
    },
    tooltip: {
        headerFormat: '<b>{point.x}</b><br/>',
        pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
    },
    
    exporting: {
       enabled:true
    },
 
   
    
    plotOptions: {
        column: {
            stacking: 'normal',
            dataLabels: {
                enabled: false
            }
        }
    },
    series: [{
        name: 'Off hours',
        data: [5, 3, 4, 7, 2,5, 3, 4, 7, 25, 3, 4, 7, 25, 3, 4, 7, 25, 3, 4, 7, 25, 3, 4, 7, 25, 3, 4, 7, 2],
        color:'#3A9AFC'
    }, {
        name: 'Free hours',
        data: [2, 2, 3, 2, 15, 3, 4, 7, 25, 3, 4, 7, 25, 3, 4, 7, 25, 3, 4, 7, 25, 3, 4, 7, 25, 3, 4, 7, 2],
        color:'#6554C0'
    }, {
        name: 'Normal-hours',
        data: [3, 4, 4, 2, 55, 3, 4, 7, 25, 3, 4, 7, 25, 3, 4, 7, 25, 3, 4, 7, 25, 3, 4, 7, 25, 3, 4, 7, 2],
        color:'#FFAB00'
    }, {
        name: 'Peak-hours',
        data: [3, 4, 4, 2, 55, 3, 4, 7, 25, 3, 4, 7, 25, 3, 4, 7, 25, 3, 4, 7, 25, 3, 4, 7, 25, 3, 4, 7, 2],
        color:'#36B37E'
    }
    
    
    
    
    ]
}

HC_exporting(Highcharts);

    }
  }


