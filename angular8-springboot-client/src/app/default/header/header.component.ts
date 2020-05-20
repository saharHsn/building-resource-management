import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {User} from '../../user/user';
import {AuthenticationService} from '../../_services';
import {UserService} from '../../user/user.service';
import {Router} from '@angular/router';
import {MatDialog} from '@angular/material';
import swal from 'sweetalert2';
import {AppService} from 'src/app/_services/app.service';
import {MessageService} from 'src/app/_services/message.service';

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
    private messages: MessageService,

  ) {
    this.currentUser = this.authenticationService.currentUserValue;
    this.messages.listen().subscribe((m:any)=>{
      
    //listening changes at button-building 
      this.getMessages();
   })
  }

  listNotifications: any;
  matBadge: number;
  listProfile = ['View profile', 'Log out'];

  ngOnInit() {
    //getting messages
    this.getMessages();


    //read
  /* this.read('5c6e04ce-558a-4d50-b147-0fa6308023b6','hello'); */

  }



  // reading message change the status
  read(idmessage,message) {

     console.log(message);

    swal.fire({
      title: 'Notification',
      text: message,
      confirmButtonText: 'Ok'
    })

    this.messages.readMessages(idmessage).subscribe(data => {
      console.log(data);
       this.getMessages();
    });
    
    /* this.router.navigateByUrl('/notifications');  */

  }

  getMessages() {
    this.messages.getMessages().subscribe(data => {
      // taking unread messages
      this.listNotifications = data.content.filter(read => {
        return read.read === false;
      });

      //if the read message is 0 do not show messages
      if( this.listNotifications.length===0){
        this.matBadge=null;
      }
      else{
        this.matBadge = this.listNotifications.length;
      }
     
      console.log(this.listNotifications);
      console.log(this.listNotifications.length);
    });
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
