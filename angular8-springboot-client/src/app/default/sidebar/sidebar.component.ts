import {Component, OnInit} from '@angular/core';
import {AppService} from '../../_services/app.service';
import {AuthenticationService} from '../../_services';
import {Router} from '@angular/router';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

  constructor(private authenticationService: AuthenticationService,
              private router: Router,
              private appService: AppService) {
  }

  ngOnInit() {
  }


  navigate(navigate: string) {
    console.log('clicked');
    // this.appService.setUserLoggedIn(false);
    // this.authenticationService.logout();
    setTimeout('', 2000);
    this.router.navigate(['/cost']).then(r => console.log('Inja'));
    setTimeout('', 2000);
    // this.router.navigate(['/login']);
  }
}
