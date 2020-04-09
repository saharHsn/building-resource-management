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

  download() {
    this.chartService.download()
      .subscribe(response => this.downLoadFile(response, 'application/ms-excel'));
  }

  downLoadFile(data: any, type: string) {
    const blob = new Blob([data], {type});
    const url = window.URL.createObjectURL(blob);
    const pwa = window.open(url);
    if (!pwa || pwa.closed || typeof pwa.closed === 'undefined') {
      alert('Please disable your Pop-up blocker and try again.');
    }
  }
}
