import {Component, OnInit} from '@angular/core';
import {User} from '../../../user/user';
import {AlertService, AuthenticationService} from '../../../_services';
import {UserService} from '../../../user/user.service';
import {first} from 'rxjs/operators';

@Component({
  selector: 'app-general',
  templateUrl: './general.component.html',
  styleUrls: ['./general.component.css']
})
export class GeneralComponent implements OnInit {
  currentUser: User;
  user: User;
  submitted = false;
  loading = false;


  constructor(private authService: AuthenticationService,
              private userService: UserService,
              private alertService: AlertService) {
    this.currentUser = this.authService.currentUserValue;
  }

  ngOnInit() {
    this.reloadData();
  }

  reloadData() {
    this.user = this.currentUser;
  }

  finishFunction() {
    this.submitted = true;
    this.save();
    this.submitted = false;
  }

  save() {
    this.alertService.clear();
    this.loading = true;
    this.userService.updateUser(this.user)
      .pipe(first())
      .subscribe(
        data => {
          // this.userInfo.next(data);
          this.loading = false;
          this.authService.updateCurrentUser(this.user);
          this.alertService.success('Your information has been changed successfully.\n', true);
        }, error => {
          this.alertService.error('Unknown Error!');
          this.loading = false;
          console.log(error);
        }
      );
  }

}
