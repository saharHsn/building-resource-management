import {Component, OnInit,Output,EventEmitter} from '@angular/core';
import {User} from '../../user/user';
import {AuthenticationService} from '../../_services';
import {UserService} from '../../user/user.service';
import {Router} from '@angular/router';
import { MatDialog } from '@angular/material';
import { NotificationsComponent } from '../notifications/notifications.component';
import { AppService } from 'src/app/_services/app.service';

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
    private appService: AppService
    
  ) {
    this.currentUser = this.authenticationService.currentUserValue;
    // this.router.navigate(['/overall']);
  }
listNotifications=[{
  title:'View notifications and insights about your consumption',
  read:false

}];

listProfile=['View profile','Log out']
  ngOnInit() {
  
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
  read(){
    
     }
     // log out button profile
     logout() {
     
      this.appService.setUserLoggedIn(false);
      // this.router.navigate(['/']);
      this.authenticationService.logout();
      this.router.navigate(['/login']);
    }

    //open and close sidebar

    toggleSideBar() {
      this.toggleSideBarForMe.emit();
      setTimeout(() => {
        window.dispatchEvent(
          new Event('resize')
        );
      }, 300);
    }
}
