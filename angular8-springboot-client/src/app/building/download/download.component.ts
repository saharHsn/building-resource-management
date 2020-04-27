import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from 'src/app/_services';
import {User} from 'src/app/_models';

@Component({
  selector: 'app-download',
  templateUrl: './download.component.html',
  styleUrls: ['./download.component.css']
})
export class DownloadComponent implements OnInit {
  currentUser: User;

  constructor(private authenticationService: AuthenticationService) {
    this.currentUser = this.authenticationService.currentUserValue;
  }

  ngOnInit() {
  }

}
