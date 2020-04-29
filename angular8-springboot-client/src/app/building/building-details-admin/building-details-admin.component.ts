import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Building} from '../model/building';
import {BuildingService} from '../service/building.service';
import {BuildingAge} from '../enums/buildingAge';
import {User} from '../../user/user';
import {BehaviorSubject} from 'rxjs';
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
  // @ts-ignore
  buildingInfo: BehaviorSubject<object> = new BehaviorSubject<object>(0);

  selectedFiles: FileList;
  currentFile: File;
  electricityFile: File;

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
      if (this.building != null) {
        this.building.builtIn = BuildingAge[this.building.age];
      } else {
        this.building = new Building();
      }
    }, error => console.log(error));
  }

  /*save() {
    this.building.owner = this.user;
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
          // @ts-ignore
          // localStorage.setItem('currentUser', JSON.stringify(data.content.owner));
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

  selectFile(event, billType: string) {
    this.selectedFiles = event.target.files;
    this.currentFile = this.selectedFiles.item(0);
    // 4 3 megs that for all bill years
    if (this.currentFile.size > (8000000)) {
      alert('File is too big! Maximum upload size is : 8MB');
      this.currentFile = null;
    }
    if (billType === 'Electricity') {
      if (this.currentFile == null) {
        this.electricityFileVariable.nativeElement.value = '';
      }
      this.electricityFile = this.currentFile;
    }
  }

  removeSelectedFile(file: HTMLInputElement) {
    file.value = '';
  }*/

  deleteBuildingBills() {
    this.buildingService.deleteAllBills(this.building.id).subscribe(
      data => {
        this.alertService.success('All bills of this building were deleted  successfully.', true);
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
    ;
  }
}

