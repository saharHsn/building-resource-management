import {BehaviorSubject} from 'rxjs';
import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Building} from '../building/model/building';
import {BuildingService} from '../building/service/building.service';
import {UserService} from '../user/user.service';
import {User} from '../user/user';
import {BuildingFileService} from '../building/service/buildingFile.service';
import {HttpResponse} from '@angular/common/http';
import {BillType} from '../building/enums/BillType';
import {AlertService, AuthenticationService} from '../_services';

@Component({
  templateUrl: './wizard-form.component.html',
  styleUrls: ['./wizard-form.component.css']
})
export class WizardFormComponent implements OnInit {
  currentUser: User;

  constructor(
    private route: ActivatedRoute,
    private buildingService: BuildingService,
    private userService: UserService,
    private fileService: BuildingFileService,
    private authService: AuthenticationService,
    private alertService: AlertService,
    private router: Router) {
    this.currentUser = this.authService.currentUserValue;
  }

  building: Building = new Building();
  user: User;
  submitted = false;
  loading = false;

  // @ts-ignore
  userInfo: BehaviorSubject<object> = new BehaviorSubject<object>(0);

  // @ts-ignore
  buildingInfo: BehaviorSubject<object> = new BehaviorSubject<object>(0);

  selectedFiles: FileList;
  currentFile: File;
  gasFile: File;
  waterFile: File;
  electricityFile: File;

  @ViewChild('gasFile', {static: false})
  gasFileVariable: ElementRef;
  @ViewChild('waterFile', {static: false})
  waterFileVariable: ElementRef;
  @ViewChild('electricityFile', {static: false})
  electricityFileVariable: ElementRef;

  getUserInfo() {
    return this.userInfo.asObservable();
  }

  getBuildingInfo() {
    return this.buildingInfo.asObservable();
  }

  ngOnInit() {
    this.reloadData();
  }

  reloadData() {
    const buildingId = this.route.snapshot.params.buildingId;
    this.buildingService.getBuilding(buildingId).subscribe(
      data => {
        this.building = data.content ? data.content : this.building;
        this.user = this.building.owner;
      },
      error => console.log(error)
    );
    /* this.userService.getUser(userId).subscribe(
       data => {
         this.user = data.content;
         this.buildingService.getBuildingByOwner(this.user).subscribe(
           data1 => {
             this.building = data1.content ? data1.content : this.building;
           },
           error => console.log(error)
         );
       }
     );*/
  }

  onSubmit() {
    // this.submitted = true;
    // this.save();
  }

  save() {
    this.building.owner = this.user;
    this.building.gasBill = this.gasFile;
    this.building.electricityBill = this.electricityFile;
    this.building.waterBill = this.waterFile;
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

  private uploadFile(file: File, buildingId: string, billType: BillType) {
    this.fileService.uploadFile(file, buildingId, billType).subscribe(event => {
      if (event instanceof HttpResponse) {
        console.log('File is completely uploaded!');
      }
    });
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
    if (billType === 'Water') {
      if (this.currentFile == null) {
        this.waterFileVariable.nativeElement.value = '';
      }
      this.waterFile = this.currentFile;
    } else if (billType === 'Gas') {
      if (this.currentFile == null) {
        this.gasFileVariable.nativeElement.value = '';
      }
      this.gasFile = this.currentFile;
    } else if (billType === 'Electricity') {
      if (this.currentFile == null) {
        this.electricityFileVariable.nativeElement.value = '';
      }
      this.electricityFile = this.currentFile;
    }
  }

  removeSelectedFile(file: HTMLInputElement) {
    file.value = '';
  }
}
