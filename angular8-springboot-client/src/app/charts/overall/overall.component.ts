import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ChartService} from '../chartService';
import {CurrentMonthSummary} from './CurrentMonthSummary';
import {User} from 'src/app/_models';
import {Building} from 'src/app/building/model/building';
import {BuildingService} from 'src/app/building/service/building.service';
import {AuthenticationService} from 'src/app/_services';
import {CurrentBuildingService} from 'src/app/_services/current-building.service';
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
              private buildingUpdateService: CurrentBuildingService,
              private messages: MessageService
              // private prediction: PredictionsComponent
  ) {
    this.currentUser = this.authService.currentUserValue;
  }

  id: number;
  currentMonthSummary: CurrentMonthSummary;
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
       /*  const id = data.content[0].id;
        this.buildingUpdateService.setIdBuilding(id);  */


       console.log(this.buildings);

      },
      error => console.log(error)
    );
  }
/*   select(event) {
    this.buildingUpdateService.setIdBuilding(event);
    this.initCharts();
    this.predictionsComponent.ngOnInit();
    this.beScoreComponent.ngOnInit();
  } */

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

  loadPage(): void {

    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() =>
    this.router.navigate(['overall']));

  }


}
