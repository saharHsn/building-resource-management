import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ChartService} from '../chartService';
import {LastMonthSummary} from './LastMonthSummary';
import {User} from 'src/app/_models';
import {Building} from 'src/app/building/model/building';
import {BuildingService} from 'src/app/building/service/building.service';
import {AuthenticationService} from 'src/app/_services';
import {PredictionsComponent} from './predictions/predictions.component';
import {BeScoreComponent} from './be-score/be-score.component';
import {MessageService} from 'src/app/_services/message.service';

@Component({
  templateUrl: './overall.component.html',
  styleUrls: ['./overall.component.css']
})
export class OverallComponent implements OnInit {
  @ViewChild(PredictionsComponent, {static: false}) predictionsComponent: PredictionsComponent;
  @ViewChild(BeScoreComponent, {static: false}) beScoreComponent: BeScoreComponent;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private chartService: ChartService,
              private buildingService: BuildingService,
              private authService: AuthenticationService,
              private messages: MessageService
              // private prediction: PredictionsComponent
  ) {
    this.currentUser = this.authService.currentUserValue;
  }

  id: number;
  lastMonthSummary: LastMonthSummary;
  currentUser: User;
  building: Building = new Building();

  // gauge variables
  monthSummary: any;
  beScore: number;
  nationalMedian: number;
  propertyTarget: number;
  buildings: any;


  ngOnInit() {
    this.monthSummary = this.messages.getMonth();
    this.initCharts();
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
      },
      error => console.log(error)
    );
  }

  initCharts() {
    this.id = this.route.snapshot.params.id;
    this.lastMonthSummary = new LastMonthSummary();
    this.reloadData();
    this.chartService.lastMonthSummary()
      .subscribe(data => {
        this.lastMonthSummary.consumption = data.content.consumption;
        this.lastMonthSummary.cost = data.content.cost;
        this.lastMonthSummary.environmental = data.content.environmental;
        this.lastMonthSummary.lastMonth = data.content.lastMonth;
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

  loadPage(): void {
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() =>
      this.router.navigate(['overall']));

  }
}
