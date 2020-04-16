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
 
  constructor(private _chartService:ChartService) {
   const year = new Date().getFullYear();
   const month =new Date().getMonth()+1;
    console.log(year);
    console.log(month);
   this._chartService.historicalConsumption().pipe(first()).subscribe(
    data=>{
 this.highcharts=Highcharts;
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
    data: data.content.offValues,
    color:'#3A9AFC'
}, {
    name: 'Free hours',
    data: data.content.freeValues,
    color:'#6554C0'
}, {
    name: 'Normal-hours',
    data: data.content.normalValues,
    color:'#FFAB00'
}, {
    name: 'Peak-hours',
    data: data.content.peakValues,
    color:'#36B37E'
}
]



}

HC_exporting(Highcharts);
   


  })








  }

  ngOnInit() {

   
    }

    create(event:MouseEvent){
        console.log(event.srcElement['value']);
        
    } 


    initChart(){
        this._chartService.historicalConsumption().pipe(first()).subscribe(
            data=>{
         this.highcharts=Highcharts;
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
            data: data.content.offValues,
            color:'#3A9AFC'
        }, {
            name: 'Free hours',
            data: data.content.freeValues,
            color:'#6554C0'
        }, {
            name: 'Normal-hours',
            data: data.content.normalValues,
            color:'#FFAB00'
        }, {
            name: 'Peak-hours',
            data: data.content.peakValues,
            color:'#36B37E'
        }
        ]
        
        
        
        }
        
        HC_exporting(Highcharts);
           
        
        
          })
    }
  }


