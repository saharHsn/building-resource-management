import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-consumption',
  templateUrl: './consumption.component.html',
  styleUrls: ['./consumption.component.css']
})
export class ConsumptionComponent implements OnInit {
  chartOptions;
  constructor() {
  }

  ngOnInit() {
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
