import {Component, OnInit} from '@angular/core';
import {ChartService} from '../chartService';

@Component({
  selector: 'app-average-solar',
  templateUrl: './average-solar.component.html',
  styleUrls: ['./average-solar.component.css']
})
export class AverageSolarComponent implements OnInit {

   solarSaving: any;

   averageSolarReq: any;

  constructor(private _chartService: ChartService) {

   this._chartService.getSolarSaving('2020').subscribe(data => {
  this.solarSaving = data;
    });
   this._chartService.getAverageSolarReq('2020').subscribe(data => {

      this.averageSolarReq = data;
      console.log(this.averageSolarReq);

    });



  }

  ngOnInit(): void {
  }

}
