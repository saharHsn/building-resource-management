import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ChartService} from '../chartService';
import {CurrentMonthSummary} from './CurrentMonthSummary';

@Component({
  templateUrl: './overall.component.html',
  styleUrls: ['./overall.component.css']
})
export class OverallComponent implements OnInit {
  id: number;
  currentMonthSummary: CurrentMonthSummary;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private chartService: ChartService) {
  }

  ngOnInit() {
    this.id = this.route.snapshot.params.id;
    this.currentMonthSummary = new CurrentMonthSummary();
    this.chartService.currentMonthSummary()
      .subscribe(data => {
        console.log(data);
        this.currentMonthSummary.consumption = data.content.consumption;
        this.currentMonthSummary.cost = data.content.cost;
        this.currentMonthSummary.environmental = data.content.environmental;
      }, error => console.log(error));
  }

  list() {
    this.router.navigate(['users']);
  }
}
