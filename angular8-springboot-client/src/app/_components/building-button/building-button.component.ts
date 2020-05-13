import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ChartService } from '../../charts/chartService';
import { BuildingService } from '../../building/service/building.service';
import { AuthenticationService } from '../../_services/authentication.service';
import { BuildingUpdateService } from '../../_services/building-update.service';
import { MessageService } from '../../_services/message.service';

@Component({
  selector: 'app-building-button',
  templateUrl: './building-button.component.html',
  styleUrls: ['./building-button.component.css']
})
export class BuildingButtonComponent implements OnInit {

  constructor(private route: ActivatedRoute,
    private router: Router,
    private chartService: ChartService,
    private buildingService: BuildingService,
    private authService: AuthenticationService,
    private buildingUpdateService: BuildingUpdateService,
    private messages: MessageService) {
  }
  buildings: any;
  id: number;
  public selectedValue: string;


  
  ngOnInit() {
    this.getBuildingUsers();
  }


  select(event) {
    this.buildingUpdateService.setIdBuilding(event);
    /*  this.initCharts(); */
    /*   this.predictionsComponent.ngOnInit();
      this.beScoreComponent.ngOnInit(); */
  }


  getBuildingUsers() {
    this.buildingService.getBuildingUsersTest().subscribe(
      data => {

        this.buildings = data.content;
        console.log(this.buildings)
        const id = data.content[0].id;
        this.selectedValue = data.content[0].name;
        console.log(this.selectedValue);

        this.buildingUpdateService.setIdBuilding(id);



        /*  this.buildings = data.content;
         console.log(this.buildings)
         if (this.buildingUpdateService.getIdBuilding() !== null) {
           return;
         } else {
           const id = data.content[0].id;
           
 
           this.buildingUpdateService.setIdBuilding(id);
         } */
      },
      error => console.log(error)
    );
  }
}
