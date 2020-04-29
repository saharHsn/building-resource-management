import {Component, OnInit} from '@angular/core';
import {User} from '../../user/user';
import {BuildingService} from '../../building/service/building.service';
import {UserService} from '../../user/user.service';
import {BuildingFileService} from '../../building/service/buildingFile.service';
import {AuthenticationService} from '../../_services';
import {Router} from '@angular/router';

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})
export class AdminPanelComponent implements OnInit {
  currentUser: User;

  constructor(private buildingService: BuildingService,
              private userService: UserService,
              private fileService: BuildingFileService,
              private authService: AuthenticationService,
              private router: Router) {
    this.currentUser = this.authService.currentUserValue;
  }

  ngOnInit() {
  }

  onSubmit() {
    // this.submitted = true;
    // this.save();
  }
}
