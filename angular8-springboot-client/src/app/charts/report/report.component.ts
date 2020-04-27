import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ChartService} from '../chartService';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent implements OnInit {
  beScore: number;
  nationalMedian: number;
  propertyTarget: number;
  constructor(private route: ActivatedRoute, private router: Router,
              private chartService: ChartService) {
  }

  ngOnInit() {
    this.chartService.getBEScore()
    .subscribe(data => {
      this.beScore = data.content;
    }, error => console.log(error));
    this.chartService.getNationalMedian()
    .subscribe(data => {
      this.nationalMedian = data.content;
    }, error => console.log(error));
    this.chartService.getPropertyTarget()
    .subscribe(data => {
      this.propertyTarget = data.content;
    }, error => console.log(error));
  }

}
