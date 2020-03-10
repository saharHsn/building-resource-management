import {Building} from '../model/building';
import {Component, OnInit} from '@angular/core';
import {BuildingService} from '../service/building.service';
import {ActivatedRoute, Router} from '@angular/router';
import {BuildingAge} from '../enums/buildingAge';

@Component({
  selector: 'app-building-details',
  templateUrl: './building-details.component.html',
  styleUrls: ['./building-details.component.css']
})
export class BuildingDetailsComponent implements OnInit {

  id: number;
  building: Building;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private buildingService: BuildingService) {
  }

  ngOnInit() {
    // this.building = new Building();
    this.id = this.route.snapshot.params.id;
    this.buildingService.getCurrentBuilding()
      .subscribe(data => {
        console.log('Building Info: ' + data);
        this.building = data.content;
        if (this.building != null) {
          this.building.builtIn = BuildingAge[this.building.age];
        } else {
          this.building = new Building();
        }
      }, error => console.log(error));
  }

  /* list() {
     this.router.navigate(['buildings']);
   }*/
}
