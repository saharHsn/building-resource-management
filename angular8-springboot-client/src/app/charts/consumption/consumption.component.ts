import {Component, OnInit} from '@angular/core';
import {ChartService} from '../chartService';

@Component({
  selector: 'app-consumption',
  templateUrl: './consumption.component.html',
  styleUrls: ['./consumption.component.css']
})
export class ConsumptionComponent implements OnInit {
  chartOptions;

  constructor(private _chartService: ChartService) {
  }

  ngOnInit() {
    /*this._chartService.historicalConsumption().subscribe(res => {

      console.log(res);


    })*/
  }

  /*getChangedSeries($event) {
    this.dataSeries = $event;
    // this.dynamicChart.reloadChart(this.dataSeries, null);
    console.log(this.dataSeries);
  }*/

  getChangedOptions($event) {
    this.chartOptions = $event;
    // this.dynamicChart.reloadChart(this.dataSeries, null);
    console.log(this.chartOptions);
  }

  /* getChangedAxis($event) {
     this.xAxisCategories = $event;
     // this.dynamicChart.reloadChart(null, this.xAxisCategories);
     console.log(this.xAxisCategories);
   }*/
}
