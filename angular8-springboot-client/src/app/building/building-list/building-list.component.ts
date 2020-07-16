import {BuildingService} from '../service/building.service';
import {Building} from '../model/building';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-building-list',
  templateUrl: './building-list.component.html',
  styleUrls: ['./building-list.component.css']
})
export class BuildingListComponent implements OnInit {
  buildings: Building[];

  constructor(private buildingService: BuildingService,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.reloadData();
  }

  reloadData() {
    this.buildingService.getBuildingsList().subscribe(data => {
        this.buildings = data.content;
        // this.alertService.success('Your dashboard will be rendered in a few minutes.\n', true);
      }
      , error => {
        console.log(error);
      });
  }

  deleteBuilding(id: string) {
    this.buildingService.deleteBuilding(id)
      .subscribe(
        data => {
          console.log(data);
          this.reloadData();
        },
        error => console.log(error));
  }

  buildingDetails(id: string) {
    // this.route.(['/building-details-admin', id]);
  }
}
