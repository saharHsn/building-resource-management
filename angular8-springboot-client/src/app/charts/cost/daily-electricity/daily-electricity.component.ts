import { Component, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts';
import { first } from 'rxjs/operators';
import { ChartService } from '../../chartService';
import { fadeInItems } from '@angular/material';


interface Select {
  value: string;
  viewValue: string;
}
@Component({
  selector: 'app-daily-electricity',
  templateUrl: './daily-electricity.component.html',
  styleUrls: ['./daily-electricity.component.css']
})
export class DailyElectricityComponent implements OnInit {
  highcharts = Highcharts;
    chartOptions: any;
    loading = true;
    //select values
    months: Select[] = [
      {value: '1', viewValue: 'January'},
      {value: '2', viewValue: 'February'},
      {value: '3', viewValue: 'March'},
      {value: '4', viewValue: 'April'},
      {value: '5', viewValue: 'May'},
      {value: '6', viewValue: 'June'},
      {value: '7', viewValue: 'July'},
      {value: '8', viewValue: 'August'},
      {value: '9', viewValue: 'September'},
      {value: '10', viewValue: 'October'},
      {value: '11', viewValue: 'November'},
      {value: '12', viewValue: 'December'},
    ]
    years: Select[] = [
      {value: '2019', viewValue: '2019'},
      {value: '2020', viewValue: '2020'},
    
    ];
       //data for chart
       yearChart:any;
       monthChart:any;
  constructor(private _chartService: ChartService) {
    this.yearChart =  String(new Date().getFullYear().toString());  
    this.monthChart  =  String( new Date().getMonth() );     

   console.log(this.yearChart);
   console.log(this.monthChart);
   }

  ngOnInit() {
    this.initChart(this.monthChart,this.yearChart);
  }

  create() {
    console.log(this.monthChart);
     console.log(this.yearChart);
     this.initChart(this.monthChart,this.yearChart);

  }

  initChart(month,year){
        
    this._chartService.historicalCost(month,year).pipe(first()).subscribe(
        data => {
            this.highcharts = Highcharts;
            this.chartOptions = {
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
                     categories: data.content.xvalues, 
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
                    enabled: true
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
                    data: data.content.offValues,
                    color: '#3A9AFC'
                }, {
                    name: 'Free hours',
                    data: data.content.freeValues,
                    color: '#6554C0'
                }, {
                    name: 'Normal-hours',
                    data: data.content.normalValues,
                    color: '#FFAB00'
                }, {
                    name: 'Peak-hours',
                    data: data.content.peakValues,
                    color: '#36B37E'
                }
                ]



            }

        },
         () => {
        });  
}
}
