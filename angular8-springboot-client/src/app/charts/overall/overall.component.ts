import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  templateUrl: './overall.component.html',
  styleUrls: ['./overall.component.css']
})
export class OverallComponent implements OnInit {

  id: number;

  constructor(private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit() {

    this.id = this.route.snapshot.params.id;

    /*this.userService.getUser(this.id)
      .subscribe(data => {
        console.log(data)
        this.user = data;
      }, error => console.log(error));*/
  }

  list() {
    this.router.navigate(['users']);
  }
}
