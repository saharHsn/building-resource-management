import {Component, OnInit} from '@angular/core';

import {User} from '../user/user';
import {AuthenticationService} from '../_services';
import {UserService} from '../user/user.service';

@Component({templateUrl: 'home.component.html'})
export class HomeComponent implements OnInit {
  currentUser: User;
  users = [];

  constructor(
    private authenticationService: AuthenticationService,
    private userService: UserService
  ) {
    this.currentUser = this.authenticationService.currentUserValue;
  }

  ngOnInit() {
  }
}
