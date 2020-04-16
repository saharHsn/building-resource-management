import { Component, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts';




  
@Component({
  selector: 'app-bullet-chart',
  templateUrl: './bullet-chart.component.html',
  styleUrls: ['./bullet-chart.component.css']
})
export class BulletChartComponent implements OnInit {
  Highcharts = Highcharts;
  chartOptions = {};

  constructor(){}
ngOnInit(){
  this.chartOptions={
    chart: {
      inverted: true,
      marginLeft: 135,
      type: 'bullet'
  },
  title: {
      text: null
  },
  legend: {
      enabled: false
  },  
   xAxis: {
    categories: ['<span class="hc-cat-title">Revenue</span><br/>U.S. $ (1,000s)']
},
  yAxis: {
    plotBands: [{
        from: 0,
        to: 150,
        color: '#666'
    }, {
        from: 150,
        to: 225,
        color: '#999'
    }, {
        from: 225,
        to: 9e9,
        color: '#bbb'
    }],
    title: null
},
  plotOptions: {
      series: {
          pointPadding: 0.25,
          borderWidth: 0,
          color: '#000',
          targetOptions: {
              width: '200%'
          }
      }
  },series: [{
    data: [{
        y: 22,
        target: 27
    }]
}],
tooltip: {
    pointFormat: '<b>{point.y}</b> (with target at {point.target})'
}
,
  credits: {
      enabled: false
  },
  exporting: {
      enabled: false
  }
}
  }


}

  

    
    
  








  


