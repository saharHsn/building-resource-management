import {BehaviorSubject} from 'rxjs';
import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {Building} from '../building/model/building';
import {BuildingService} from '../building/service/building.service';
import {UserService} from '../user/user.service';
import {User} from '../user/user';
import {BuildingFileService} from '../building/service/buildingFile.service';
import {HttpResponse} from '@angular/common/http';
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
  // @ts-ignore
  userInfo: BehaviorSubject<object> = new BehaviorSubject<object>(0);

  // @ts-ignore
  buildingInfo: BehaviorSubject<object> = new BehaviorSubject<object>(0);

  selectedFiles: FileList;
  currentFile: File;
  gasFile: File;
  waterFile: File;
  electricityFile: File;

  @ViewChild('gasFile', null)
  gasFileVariable: ElementRef;
  @ViewChild('waterFile', null)
  waterFileVariable: ElementRef;
  @ViewChild('electricityFile', null)
  electricityFileVariable: ElementRef;

  getUserInfo() {
    return this.userInfo.asObservable();
  }

  getBuildingInfo() {
    return this.buildingInfo.asObservable();
  }

  ngOnInit() {

  }

  onSubmit() {
    // this.submitted = true;
    // this.save();
  }

  save() {
    /*this.userService.createUser(this.user)
      .subscribe(data => this.userInfo.next(data), error => console.log(error));
*/
    this.building.owner = this.user;
    this.building.gasBill = this.gasFile;
    this.building.electricityBill = this.electricityFile;
    this.building.waterBill = this.waterFile;
    console.log(this.building.name);
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
    this.submitted = false;
  }

  selectFile(event, billType: string) {
    this.selectedFiles = event.target.files;
    this.currentFile = this.selectedFiles.item(0);
    if (this.currentFile.size > 3000000) {
      alert('File is too big!');
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
