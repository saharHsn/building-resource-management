import { Component, OnInit } from '@angular/core';

import * as Highcharts from 'highcharts';

import HC_map from 'highcharts/modules/map';
HC_map(Highcharts);
import { first } from 'rxjs/operators';
import { ChartService } from '../../chartService';
import { Router } from '@angular/router';
import { TimeServicesService } from '../../../_services/time-services.service';
@Component({
    selector: 'app-heatmap-hourly',
    templateUrl: './heatmap-hourly.component.html',
    styleUrls: ['./heatmap-hourly.component.css']
})
export class HeatmapHourlyComponent implements OnInit {

    Highcharts = Highcharts;
    loading = true;
    chartOptions: any;
    data:any;
    loadingBar:boolean=false; 
    months: any = this._timeServices.getMonths();
    years:any= this._timeServices.getYears();
    monthChart=this._timeServices.currentMonth();
    yearChart=this._timeServices.currentYear();


    constructor(private chartService:ChartService,private _timeServices:TimeServicesService) { 
     

    
    }

    ngOnInit(): void {
      this.initChart(this.monthChart,this.yearChart);
    }


    create() {
        this.loadingBar=true;
        this.initChart(this.monthChart, this.yearChart);
      } 

    initChart(month,year){
        this.chartService.getHeatMapHourly(month,year).subscribe(data=>{
            this.data=data.content.dataMatrix;
            this.loadingBar=false;
            this.chartOptions = {
             chart: {
               type: 'heatmap',
               marginTop: 40,
               marginBottom: 80,
               plotBorderWidth: 1
           },



           title: {
               text: 'Consumption Heat Map'
           },

           xAxis: {
               categories: [
              
               ],title: {
                   text: 'Days'
               }
           },

           yAxis: {
               categories: ['1',
                   '2',
                   '3',
                   '4',
                   '5',
                   '6',
                   '7',
                   '8',
                   '9',
                   '10',
                   '11',
                   '12',
                   '13',
                   '14',
                   '15',
                   '16',
                   '17',
                   '18',
                   '19',
                   '20',
                   '21',
                   '22',
                   '23',
                   '24',], title: {
                       text: 'Hours'
                   },

               reversed: true,
               tickInterval: 1,
               startOnTick: false,
               endOnTick: false,
           },
           /* 
               accessibility: {
                   point: {
                       descriptionFormatter: function (point) {
                           var ix = point.index + 1,
                               xName = getPointCategoryName(point, 'x'),
                               yName = getPointCategoryName(point, 'y'),
                               val = point.value;
                           return ix + '. ' + xName + ' sales ' + yName + ', ' + val + '.';
                       }
                   }
               }, */

           colorAxis: {
               linearGradient: [300, 300, 300, 0],
               stops: [
                 [0.1, '#55BF3B'],
                 [0.6, '#FFF700'],
                 [0.7, '#FFF700'],
                 [0.95, '#FF0000'], // green
                 // yellow
                 // red
               ]
           },

           legend: {
               align: 'right',
               layout: 'vertical',
               margin: 0,
               verticalAlign: 'top',
               y: 25,
               symbolHeight: 280
           },
         
           tooltip: {
            headerFormat: '<b>{series.name}</b><br>',
            pointFormat: ' Day {point.x} ,Hour {point.y} Value {point.value}',
         
        }, 
           credits: {
               enabled: false
           },
           series: [{
               name: 'kWh',
               borderWidth: 1,
               data:  this.data,
               dataLabels: {
                   enabled: false,
                   color: '#000000'
               }
           }],

           responsive: {
               rules: [{
                   condition: {
                       maxWidth: 500
                   },
                   chartOptions: {
                       yAxis: {
                           labels: {
                               formatter: function () {
                                   return this.value.charAt(0);
                               }
                           }
                       }
                   }
               }]
           }

       }
     });
    }
}
