import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ChartService} from '../chartService';
import {CurrentMonthSummary} from './CurrentMonthSummary';
import { User } from 'src/app/_models';
import { Building } from 'src/app/building/model/building';
import { BuildingService } from 'src/app/building/service/building.service';
import { AuthenticationService } from 'src/app/_services';

@Component({
  templateUrl: './overall.component.html',
  styleUrls: ['./overall.component.css']
})
export class OverallComponent implements OnInit {
  id: number;
  currentMonthSummary: CurrentMonthSummary;
  currentUser: User;
  building: Building = new Building();
//gauge variables
beScore: number;
nationalMedian: number;
propertyTarget: number;
  constructor(private route: ActivatedRoute,
              private router: Router,
              private chartService: ChartService,
              private buildingService: BuildingService,
              private authService: AuthenticationService) {
                this.currentUser = this.authService.currentUserValue;
  }

  ngOnInit() {
    this.id = this.route.snapshot.params.id;
    this.currentMonthSummary = new CurrentMonthSummary();
    this.reloadData();
    this.chartService.currentMonthSummary()
      .subscribe(data => {
        console.log(data);
        this.currentMonthSummary.consumption = data.content.consumption;
        this.currentMonthSummary.cost = data.content.cost;
        this.currentMonthSummary.environmental = data.content.environmental;
      
      }, error => console.log(error));
      this.chartService.getNationalMedian()
      .subscribe(data => {
        console.log(data);
        this.nationalMedian = data.content;
      }, error => console.log(error));
      this.chartService.getPropertyTarget()
      .subscribe(data => {
        console.log(data);
        this.propertyTarget = data.content;
      }, error => console.log(error));

      
  }

  list() {
    this.router.navigate(['users']);
  }
  reloadData() {
    this.buildingService.getBuildingByOwner(this.currentUser).subscribe(
      data => {
        this.building = data.content ? data.content : this.building;
      },
      error => console.log(error)
    );
  }

}
