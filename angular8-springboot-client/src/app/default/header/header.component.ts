import {Component, OnInit} from '@angular/core';
import {User} from '../../user/user';
import {AuthenticationService} from '../../_services';
import {UserService} from '../../user/user.service';
import {Router} from '@angular/router';
import { MatDialog } from '@angular/material';
import { NotificationsComponent } from '../notifications/notifications.component';

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
    private router: Router,
    public dialog: MatDialog
  ) {
    this.currentUser = this.authenticationService.currentUserValue;
    // this.router.navigate(['/overall']);
  }
listNotifications=[];
  ngOnInit() {
    this.listNotifications=this.notificationsList();
  }

  //dialog component
  openDialog(): void {
    const dialogRef = this.dialog.open(NotificationsComponent, {
      width: '400px',
     /*  data: {name: this.name, animal: this.animal} */
    });

 /*    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.animal = result;
    }); */
  }
// notifications
  notificationsList(){
    const list=["Gravida molestie ut fringilla est. Praesent sed cras enim, eleifend aliquam","Gravida molestie ut fringilla est. Praesent sed cras enim, eleifend aliquam","Gravida molestie ut fringilla est. Praesent sed cras enim, eleifend aliquam","Gravida molestie ut fringilla est. Praesent sed cras enim, eleifend aliquam"];
    return list;
  }
}
