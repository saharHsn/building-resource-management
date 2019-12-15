﻿import {Component, OnInit} from '@angular/core';

import {User} from '../user/user';
import {AuthenticationService} from '../_services';
import {UserService} from '../user/user.service';

@Component({ templateUrl: 'home.component.html' })
export class HomeComponent implements OnInit {
    currentUser: User;
    users = [];

    constructor(
        private authenticationService: AuthenticationService,
        private userService: UserService
    ) {
      this.currentUser = this.authenticationService.getCurrentUser();
    }

    ngOnInit() {
      // this.loadAllUsers();
    }

    /*deleteUser(id: number) {
        this.userService.delete(id)
            .pipe(first())
            .subscribe(() => this.loadAllUsers());
    }

    private loadAllUsers() {
        this.userService.getAll()
            .pipe(first())
            .subscribe(users => this.users = users);
    }*/
}
