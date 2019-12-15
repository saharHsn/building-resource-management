import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ChartService} from '../chartService';
import {Saving} from './Saving';

@Component({
  templateUrl: './overall.component.html',
  styleUrls: ['./overall.component.css']
})
export class OverallComponent implements OnInit {
  id: number;
  saving: Saving;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private chartService: ChartService) {
  }

  ngOnInit() {
    this.id = this.route.snapshot.params.id;
    this.saving = new Saving();
    this.chartService.savingThisMonth()
      .subscribe(data => {
        console.log(data);
        this.saving.consumption = data.content.consumption;
        this.saving.cost = data.content.cost;
        this.saving.environmental = data.content.environmental;
      }, error => console.log(error));
  }

  list() {
    this.router.navigate(['users']);
  }
}
