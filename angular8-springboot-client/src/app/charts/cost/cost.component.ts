import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-cost',
  templateUrl: './cost.component.html',
  styleUrls: ['./cost.component.css']
})
export class CostComponent implements OnInit {

  constructor(private router: Router) {
  }

  ngOnInit() {
 
  }


//recharge page, this function is sent to child component
  loadPage():void{

    this.router.navigateByUrl('/', {skipLocationChange: true}).then(()=>
    this.router.navigate(['cost']))
   
  }
}
