import {Component, OnInit} from '@angular/core';

import {ChartService} from '../../chartService';

@Component({
  selector: 'app-bullet-chart',
  templateUrl: './bullet-chart.component.html',
  styleUrls: ['./bullet-chart.component.css']
})
export class BulletChartComponent implements OnInit {
  dataSource: any;
  type: string;
  width: number;
  height: number;
  dataFormat: string;
   target: number;
   monthEnergy: number;
  constructor(private chartService: ChartService) {}

  ngOnInit() {

    this.initChart();

  }

  initChart() {
    this.chartService.getAllEnergyConsumptionIndexes()
    .subscribe((data: any) => {


      this.target = this.setvalues(data.content.energyEfficiencyLevel.propertyTargetCert);
      this.monthEnergy = this.setvalues(data.content.energyEfficiencyLevel.thisMonthCert);
      this.dataSource = {
        chart: {
          caption: null,
          theme: 'fusion',
          ticksonright: '1',
          plottooltext: 'this Month',
          targettooltext: 'Target : <b>$llll</b>',
          showTickValues: '0',
          showLimits: '0',
          showTickMarks: '0',
          showValue: '0',
          bgColor: '#fafafa'
        },


        colorrange: {
          color: [
            {
              minvalue: '0',
              maxvalue: '8',
              code: '#fafafa'
            }
          ]
        },
        value: this.monthEnergy,
        target: this.target,
      /*   showLabels: '0',
        labelDisplay: "none" */
      };
      this.width = 100;
      this.height = 320;
      this.type = 'vbullet';
      this.dataFormat = 'json';

    }, error => console.log(error));

  }

  // get the number value
  setvalues(arg) {

    let value;

    switch (arg) {
      case 'APlus':
        value = 7.5;
        break;
      case 'A':
        value = 6.5;
        break;
      case 'B':
        value = 5.5;
        break;
      case 'BMinus':
        value = 4.5;
        break;
      case 'C':
        value = 3.5;
        break;
      case 'D':
        value = 2.5;
        break;
      case 'E':
        value = 1.5;
        break;
      case 'F':
        value = .5;
        break;
      default:
        value = 0;
        break;

    }
    return value;
  }

}







