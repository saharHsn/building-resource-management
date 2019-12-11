import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ChartService} from '../../chartService';
import {EnergyConsumptionIndexDto} from "./EnergyConsumptionIndexDto";

@Component({
  selector: 'app-energy-efficiency',
  templateUrl: './energy-efficiency.component.html',
  styleUrls: ['./energy-efficiency.component.css']
})
export class EnergyEfficiencyComponent implements OnInit {
  buildingId: number;
  energyIndex: EnergyConsumptionIndexDto;

  constructor(private route: ActivatedRoute, private router: Router,
              private chartService: ChartService) {
  }

  ngOnInit() {
    this.energyIndex = new EnergyConsumptionIndexDto();
    // this.buildingId = this.route.snapshot.params.id;
    this.chartService.getAllEnergyConsumptionIndexes(this.buildingId)
      .subscribe(data => {
        console.log(data);
        this.energyIndex = data;
      }, error => console.log(error));
  }

}
