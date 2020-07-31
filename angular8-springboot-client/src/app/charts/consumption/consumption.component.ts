import {Component, OnInit} from '@angular/core';
import {ChartService} from '../chartService';
import {Router} from '@angular/router';

@Component({
  selector: 'app-consumption',
  templateUrl: './consumption.component.html',
  styleUrls: ['./consumption.component.css']
})
export class ConsumptionComponent implements OnInit {
  chartOptions;

  // tslint:disable-next-line:variable-name
  constructor(private _chartService: ChartService, private router: Router) {
  }

  ngOnInit() {

  }

  /*getChangedSeries($event) {
    this.dataSeries = $event;
    // this.dynamicChart.reloadChart(this.dataSeries, null);
  }*/

  getChangedOptions($event) {
    this.chartOptions = $event;
    // this.dynamicChart.reloadChart(this.dataSeries, null);
    console.log(this.chartOptions);
  }

  loadPage(): void {

    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() =>
    this.router.navigate(['consumption-component']));
  }
}
