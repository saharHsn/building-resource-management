import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ChartService} from '../../chartService';

@Component({
  selector: 'app-energy-efficiency',
  templateUrl: './energy-efficiency.component.html',
  styleUrls: ['./energy-efficiency.component.css']
})
export class EnergyEfficiencyComponent implements OnInit {
  energyIndex: any;

  constructor(private route: ActivatedRoute, private router: Router,
              private chartService: ChartService) {
  }

  ngOnInit() {
    // this.energyIndex = new EnergyConsumptionIndexDto();
    this.chartService.getAllEnergyConsumptionIndexes()
      .subscribe(data => {
        this.energyIndex = data.content;
      }, error => console.log(error));
  }
}
