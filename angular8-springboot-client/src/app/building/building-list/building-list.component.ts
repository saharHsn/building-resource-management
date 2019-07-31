import { BuildingDetailsComponent } from '../building-details/building-details.component';
import { Observable } from "rxjs";
import { BuildingService } from "../service/building.service";
import { Building } from "../model/building";
import { Component, OnInit } from "@angular/core";
import { Router } from '@angular/router';

@Component({
  selector: "app-building-list",
  templateUrl: "./building-list.component.html",
  styleUrls: ["./building-list.component.css"]
})
export class BuildingListComponent implements OnInit {
  buildings: Observable<Building[]>;

  constructor(private buildingService: BuildingService,
    private router: Router) {}

  ngOnInit() {
    this.reloadData();
  }

  reloadData() {
    this.buildings = this.buildingService.getBuildingsList();
  }

  deleteBuilding(id: number) {
    this.buildingService.deleteBuilding(id)
      .subscribe(
        data => {
          console.log(data);
          this.reloadData();
        },
        error => console.log(error));
  }

  buildingDetails(id: number){
    this.router.navigate(['details', id]);
  }
}
