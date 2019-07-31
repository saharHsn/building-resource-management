import {BehaviorSubject, Observable} from 'rxjs';
import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Building} from '../building/model/building';
import {BuildingService} from '../building/service/building.service';
import {UserService} from '../user/user.service';
import {User} from '../user/user';
import {BuildingFileService} from '../building/service/buildingFile.service';
import {HttpClient, HttpResponse, HttpEventType} from '@angular/common/http';
import {BillType} from '../building/enums/BillType';


@Component({
  templateUrl: './wizard-form.component.html',
  styleUrls: ['./wizard-form.component.css']
})
export class WizardFormComponent implements OnInit {

  constructor(private buildingService: BuildingService,
              private userService: UserService,
              private fileService: BuildingFileService,
              private router: Router) {
  }

  building: Building = new Building();
  user: User = new User();
  submitted = false;
  // userInfo: Object;
  // buildingInfo: Object;
  userInfo: BehaviorSubject<Object> = new BehaviorSubject<Object>(0);

  buildingInfo: BehaviorSubject<Object> = new BehaviorSubject<Object>(0);

  selectedFiles: FileList;
  currentFile: File;
  gasFile: File;
  waterFile: File;
  electricityFile: File;

  getUserInfo() {
    return this.userInfo.asObservable();
  }

  getBuildingInfo() {
    return this.buildingInfo.asObservable();
  }

  ngOnInit() {

  }

  onSubmit() {
    this.submitted = true;
    this.save();
  }

  save() {
    /*this.userService.createUser(this.user)
      .subscribe(data => this.userInfo.next(data), error => console.log(error));
*/
    this.building.owner = this.user;
    this.building.gasBill = this.gasFile;
    this.building.electricityBill = this.electricityFile;
    this.building.waterBill = this.waterFile;
    this.buildingService.createBuilding(this.building).subscribe(data => this.buildingInfo.next(data), error => console.log(error));

    /*this.uploadFile(this.gasFile, '', BillType.Gas);
    this.uploadFile(this.electricityFile, '', BillType.Electricity);
    this.uploadFile(this.waterFile, '', BillType.Water);
    */
    // this.user = new User();
    // this.building = new Building();
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
  }

  selectFile(event, billType: String) {
    this.selectedFiles = event.target.files;
    this.currentFile = this.selectedFiles.item(0);
    if (billType == 'Water') {
      this.waterFile = this.currentFile;
    } else if (billType == 'Gas') {
      this.gasFile = this.currentFile;
    } else if (billType == 'Electricity') {
      this.electricityFile = this.currentFile;
    }
  }
}
