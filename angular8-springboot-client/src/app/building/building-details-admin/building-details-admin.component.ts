import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Building} from '../model/building';
import {BuildingService} from '../service/building.service';
import {BuildingAge} from '../enums/buildingAge';
import {User} from '../../user/user';
import {AlertService} from '../../_services';

@Component({
  selector: 'app-building-details-admin',
  templateUrl: './building-details-admin.component.html',
  styleUrls: ['./building-details-admin.component.css']
})
export class BuildingDetailsAdminComponent implements OnInit {

  building: Building;
  user: User;
  submitted = false;
  loading = false;

  @ViewChild('electricityFile', null)
  electricityFileVariable: ElementRef;

  constructor(private route: ActivatedRoute,
              private buildingService: BuildingService,
              private alertService: AlertService) {
    console.log();
  }

  ngOnInit() {
    const buildingId = this.route.snapshot.params.buildingId;
    this.buildingService.getBuilding(buildingId).subscribe(data => {
      this.building = data.content;
      console.log(this.building.owner);
      if (this.building != null) {
        this.building.builtIn = BuildingAge[this.building.age];
      } else {
        this.building = new Building();
      }
    }, error => console.log(error));
  }
  deleteBuildingBills() {
    this.loading = true;
    this.buildingService.deleteAllBills(this.building.id).subscribe(
      data => {
        this.alertService.success('All bills of this building were deleted  successfully.', true);
        this.loading = false;
      },
      error => {
        let errorMessage = 'Unknown Error (Internal server error!)';
        if (error.status === 400 && error.error.errors) {
          errorMessage = error.error.errors;
        } else {
          errorMessage = error.error.message;
        }
        this.alertService.error(errorMessage);
        this.loading = false;
      });

  }
}

