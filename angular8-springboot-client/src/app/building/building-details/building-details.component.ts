import {Building} from '../model/building';
import {Component, OnInit} from '@angular/core';
import {BuildingService} from '../service/building.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-building-details',
  templateUrl: './building-details.component.html',
  styleUrls: ['./building-details.component.css']
})
export class BuildingDetailsComponent implements OnInit {

  id: number;
  building: Building;

  constructor(private route: ActivatedRoute, private router: Router,
              private buildingService: BuildingService) {
  }

  ngOnInit() {
    this.building = new Building();
    this.id = this.route.snapshot.params.id;
    /*this.buildingService.getBuilding(this.id)
      .subscribe(data => {
        console.log(data);
        this.building = data;
      }, error => console.log(error));*/
  }

  /* list() {
     this.router.navigate(['buildings']);
   }*/
}
