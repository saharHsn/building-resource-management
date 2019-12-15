import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ChartService} from '../../chartService';

@Component({
  selector: 'app-report-be-score',
  templateUrl: './report-be-score.component.html',
  styleUrls: ['./report-be-score.component.css']
})
export class ReportBeScoreComponent implements OnInit {
  beScore: number;
  nationalMedian: number;
  propertyTarget: number;


  constructor(private route: ActivatedRoute, private router: Router,
              private chartService: ChartService) {
  }

  ngOnInit() {
    this.chartService.getBEScore()
      .subscribe(data => {
        console.log(data);
        this.beScore = data.content;
      }, error => console.log(error));
    this.chartService.getNationalMedian()
      .subscribe(data => {
        console.log(data);
        this.nationalMedian = data.content;
      }, error => console.log(error));
    this.chartService.getPropertyTarget()
      .subscribe(data => {
        console.log(data);
        this.propertyTarget = data.content;
      }, error => console.log(error));
  }
}

