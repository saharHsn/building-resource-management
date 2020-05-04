import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { User } from '../../user/user';
import { AuthenticationService } from '../../_services';
import { UserService } from '../../user/user.service';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material';

import { AppService } from 'src/app/_services/app.service';
import { MessageService } from 'src/app/_services/message.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  currentUser: User;
  @Output() toggleSideBarForMe: EventEmitter<any> = new EventEmitter();

  constructor(
    private authenticationService: AuthenticationService,
    private userService: UserService,
    private router: Router,
    public dialog: MatDialog,
    private appService: AppService,
    private messages: MessageService
  ) {
    this.currentUser = this.authenticationService.currentUserValue;
    
  }

  listNotifications: any;
  matBadge: number;
  listProfile = ['View profile', 'Log out'];

  ngOnInit() {
    this.getMessages();
  }

  // dialog component
/*   openDialog(): void {
    const dialogRef = this.dialog.open(NotificationsComponent, {
      width: '400px',

    });


  } */

  // notifications
  read(idmessage) {
    this.messages.readMessages(idmessage).subscribe(data => {
      console.log(data);

    });
    this.router.navigateByUrl('/notifications');

  }
  getMessages() {
    this.messages.getMessages().subscribe(data => {
      //taking unread messages
       this.listNotifications= data.content.filter(read=>{
         return read.read===false;
       })
     
     /*this.listNotifications = data.content;*/
      this.matBadge = this.listNotifications.length
      console.log(this.listNotifications);
      console.log(this.listNotifications.length);
    })
  }

  // log out button profile
  logout() {
    this.appService.setUserLoggedIn(false);
    // this.router.navigate(['/']);
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }

  // open and close sidebar

  toggleSideBar() {
    this.toggleSideBarForMe.emit();
    setTimeout(() => {
      window.dispatchEvent(
        new Event('resize')
      );
    }, 300);
  }
}
