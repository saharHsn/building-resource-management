import {Component, OnInit} from '@angular/core';
import {User} from '../../user/user';
import {AuthenticationService} from '../../_services';
import {UserService} from '../../user/user.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  currentUser: User;

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
