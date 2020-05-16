import {Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-normalized-consumption',
  templateUrl: './normalized-consumption.component.html',
  styleUrls: ['./normalized-consumption.component.css']
})
export class NormalizedConsumptionComponent implements OnInit {

  constructor(private router: Router) {
  }

  ngOnInit() {
  }

  loadPage():void{

    this.router.navigateByUrl('/', {skipLocationChange: true}).then(()=>
    this.router.navigate(['normalized-consumption']))
   
  }

}
