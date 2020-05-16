import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Building} from '../../../building/model/building';
import {BuildingService} from '../../../building/service/building.service';
import {UserService} from '../../../user/user.service';
import {BuildingFileService} from '../../../building/service/buildingFile.service';
import {AlertService, AuthenticationService} from '../../../_services';
import {Router} from '@angular/router';
import {User} from '../../../user/user';
import {BehaviorSubject} from 'rxjs';

@Component({
  selector: 'app-building',
  templateUrl: './building.component.html',
  styleUrls: ['./building.component.css']
})
export class BuildingComponent implements OnInit {
  currentUser: User;
  building: Building = new Building();
  submitted = false;
  loading = false;
  electricityFile: File;
  // @ts-ignore
  buildingInfo: BehaviorSubject<object> = new BehaviorSubject<object>(0);
  @ViewChild('electricityFile', {static: false})
  electricityFileVariable: ElementRef;

  constructor(private buildingService: BuildingService,
              private userService: UserService,
              private fileService: BuildingFileService,
              private authService: AuthenticationService,
              private alertService: AlertService,
              private router: Router) {
    this.currentUser = this.authService.currentUserValue;
  }

  ngOnInit() {
    this.reloadData();
  }

  reloadData() {
    this.buildingService.getBuildingByOwner(this.currentUser).subscribe(
      data => {
        this.building = data.content ? data.content : this.building;
      },
      error => console.log(error)
    );
  }

  save() {
    this.building.owner = this.currentUser;
    this.building.electricityBill = this.electricityFile;
    this.loading = true;
    if (!this.building.id) {
      this.buildingService.createBuilding(this.building).subscribe(
        data => {
          this.buildingInfo.next(data);
          // @ts-ignore
          // localStorage.setItem('currentUser', JSON.stringify(data.content.owner));
          this.loading = false;
          this.alertService.success('Your dashboard will be rendered in a few minutes.\n', true);
        },
        error => {
          this.alertService.error('Unknown Error!');
          this.loading = false;
          console.log(error);
        });
    } else {
      this.buildingService.updateBuilding(this.building).subscribe(
        data => {
          this.buildingInfo.next(data);
          this.loading = false;
          this.alertService.success('Your dashboard will be rendered in a few minutes.\n', true);
        }
        , error => {
          this.alertService.error('Unknown Error!');
          this.loading = false;
          console.log(error);
        });
    }
  }

  finishFunction() {
    this.submitted = true;
    this.save();
    this.submitted = false;
  }
}
