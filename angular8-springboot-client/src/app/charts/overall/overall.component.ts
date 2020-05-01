import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ChartService} from '../chartService';
import {CurrentMonthSummary} from './CurrentMonthSummary';
import {User} from 'src/app/_models';
import {Building} from 'src/app/building/model/building';
import {BuildingService} from 'src/app/building/service/building.service';
import {AuthenticationService} from 'src/app/_services';
import {IdServiceService} from 'src/app/_services/id-service.service';

@Component({
  templateUrl: './overall.component.html',
  styleUrls: ['./overall.component.css']
})
export class OverallComponent implements OnInit {
  id: number;
  currentMonthSummary: CurrentMonthSummary;
  currentUser: User;
  building: Building = new Building();

  // gauge variables

  beScore: number;
  nationalMedian: number;
  propertyTarget: number;
  buildings: any;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private chartService: ChartService,
              private buildingService: BuildingService,
              private authService: AuthenticationService,
              private idservice: IdServiceService,
              // private prediction: PredictionsComponent
  ) {
    this.currentUser = this.authService.currentUserValue;
  }

  ngOnInit() {
    this.getBuildingUsers();
    // create charts
    this.initCharts();
    // this.prediction.ngOnInit();
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

  getBuildingUsers() {
    this.buildingService.getBuildingUsersTest().subscribe(
      data => {
        this.buildings = data.content;
        if (this.idservice.getIdbuilding() !== null) {
          return;
        } else {
          const id = data.content[0].id;
          this.idservice.setIdbuilding(id);
        }
      },
      error => console.log(error)
    );
  }

// item selected
  select(event) {
    this.idservice.setIdbuilding(event);
    this.initCharts();
    // call prediction ngOnInit
    // call BeScoreComponent ngOnInit
  }

  initCharts() {
    this.id = this.route.snapshot.params.id;
    this.currentMonthSummary = new CurrentMonthSummary();
    this.reloadData();
    this.chartService.currentMonthSummary()
      .subscribe(data => {
        this.currentMonthSummary.consumption = data.content.consumption;
        this.currentMonthSummary.cost = data.content.cost;
        this.currentMonthSummary.environmental = data.content.environmental;
      }, error => console.log(error));
    this.chartService.getNationalMedian()
      .subscribe(data => {
        this.nationalMedian = data.content;
      }, error => console.log(error));
    this.chartService.getPropertyTarget()
      .subscribe(data => {
        this.propertyTarget = data.content;
      }, error => console.log(error));
  }
}
