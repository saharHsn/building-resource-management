import { BuildingService } from '../service/building.service';
import { Building } from '../model/building';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-building',
  templateUrl: './create-building.component.html',
  styleUrls: ['./create-building.component.css']
})
export class CreateBuildingComponent implements OnInit {

  building: Building = new Building();
  submitted = false;

  constructor(private buildingService: BuildingService,
    private router: Router) { }

  ngOnInit() {
  }

  newBuilding(): void {
    this.submitted = false;
    this.building = new Building();
  }

  save() {
    this.buildingService.createBuilding(this.building)
      .subscribe(data => console.log(data), error => console.log(error));
    this.building = new Building();
    this.gotoList();
  }

  onSubmit() {
    this.submitted = true;
    this.save();    
  }

  gotoList() {
    this.router.navigate(['/buildings']);
  }
}
