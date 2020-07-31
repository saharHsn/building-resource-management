import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';
import * as Highcharts1 from 'highcharts/highcharts';
import * as HighchartsBullet from 'highcharts/modules/bullet';
import {ChartService} from '../../chartService';
// @ts-ignore
HighchartsBullet(Highcharts1);
@Component({
  selector: 'app-testbullet',
  templateUrl: './testbullet.component.html',
  styleUrls: ['./testbullet.component.css']
})
export class TestbulletComponent implements OnInit {
  Highcharts: any;
  chartOptions: any;

  // variables of bullet
  target: number;
  monthEnergy: number;
  lastYear: number;
  constructor(private chartService: ChartService) { }




  ngOnInit(): void {
    this.chartService.getAllEnergyConsumptionIndexes()
    .subscribe((data: any) => {


      this.target = this.setvalues(data.content.energyEfficiencyLevel.propertyTargetCert);
      console.log(this.target);
      this.monthEnergy = this.setvalues(data.content.energyEfficiencyLevel.thisMonthCert);
      this.lastYear = this.setvalues(data.content.energyEfficiencyLevel.baseLineCert);
      this.Highcharts = Highcharts;
      this.chartOptions = {

      chart: {
        inverted: false,
        backgroundColor: null,
        plotBackgroundColor: null,
        plotBackgroundImage: null,
      /*   marginLeft: 135,
        marginTop: 40, */
        type: 'bullet'
    },
    title: {
        text: null
    },
    legend: {
        enabled: false
    },
    xAxis: {

      labels: {
          enabled: false
      }
  },

    yAxis: {
        gridLineWidth: 0,
        max: 300,


          labels: {
              enabled: false
          },

        plotBands: [{
          from: 0,
          to: this.lastYear,
          color: '#D4D1D0'
      }],
      title: null
    },
    plotOptions: {
        series: {
            pointPadding: 0.25,
            borderWidth: 0,
            color: '#676666',
            targetOptions: {
                width: '200%',
                color: '#000'
            }
        }
    },  series: [{
      data: [{
          y: this.monthEnergy,
          target: this.target,


      }],
      showInLegend: false
  }], tooltip: {
    enabled: false
}
  ,
    credits: {
        enabled: false
    },
    exporting: {
        enabled: false
    }
};

    }, error => console.log(error));

    }




    setvalues(arg) {

      let value;

      switch (arg) {
        case 'APlus':
          value = 390;
          break;
        case 'A':
          value = 260;
          break;
        case 'B':
          value = 220;
          break;
        case 'BMinus':
          value = 180;
          break;
        case 'C':
          value = 140;
          break;
        case 'D':
          value = 100;
          break;
        case 'E':
          value = 70;
          break;
        case 'F':
          value = 30;
          break;
        default:
          value = 0;
          break;

      }
      return value;
    }
  }


