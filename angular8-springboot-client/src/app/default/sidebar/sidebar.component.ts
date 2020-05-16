import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {User} from '../../user/user';
import {AuthenticationService} from '../../_services';
import {Role} from '../../building/enums/Role';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  currentUser: User;
  isAdmin: boolean;

  constructor(private router: Router,
              private authenticationService: AuthenticationService) {
    this.isAdmin = false;
    this.authenticationService.currentUser.subscribe(x => {
        this.currentUser = x;
        // @ts-ignore
        this.isAdmin = (x.content.user.role === Role.Admin.valueOf());
      }
    );
  }

  ngOnInit() {
  }
}
