import {Component, OnInit} from '@angular/core';

import {User} from '../user/user';
import {AuthenticationService} from '../_services';
import {UserService} from '../user/user.service';
import {Router} from '@angular/router';

@Component({templateUrl: 'home.component.html'})
export class HomeComponent implements OnInit {
  currentUser: User;
  users = [];

  constructor(
    private authenticationService: AuthenticationService,
    private userService: UserService,
    private router: Router
  ) {
    this.currentUser = this.authenticationService.currentUserValue;
    // this.router.navigate(['/overall']);
  }

  ngOnInit() {
  }
}
