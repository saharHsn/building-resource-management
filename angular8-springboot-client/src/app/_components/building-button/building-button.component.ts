import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ChartService} from '../../charts/chartService';
import {BuildingService} from '../../building/service/building.service';
import {AuthenticationService} from '../../_services/authentication.service';
import {BuildingUpdateService} from '../../_services/building-update.service';
import {MessageService} from '../../_services/message.service';

@Component({
  selector: 'app-building-button',
  templateUrl: './building-button.component.html',
  styleUrls: ['./building-button.component.css']
})
export class BuildingButtonComponent implements OnInit {
  currentBuildingName: any;
/*   @ViewChild(HeaderComponent, {static: true}) headerComponent: HeaderComponent;
 */  constructor(private route: ActivatedRoute,
                 private router: Router,
                 private chartService: ChartService,
                 private buildingService: BuildingService,
                 private authService: AuthenticationService,
                 private buildingUpdateService: BuildingUpdateService,
                 private messages: MessageService,

    ) {

  }

  buildings: any;
  id: number;


  @Output('loadPage') init: EventEmitter<any> = new EventEmitter();

  ngOnInit() {

    this.buildingService.getBuildingUsersTest().subscribe(
      data => {
         this.buildings = data.content;

         this.currentBuildingName = this.buildingUpdateService.getIdBuilding();
         console.log(this.currentBuildingName);

      },
      error => console.log(error)
    );
  }


  selected(event) {
    console.log(event);
    this.buildingUpdateService.setIdBuilding(event);
     // active subscription in header
    this.messages.activeMessage();
    this.init.emit();



  }





  getBuildingUsers() {
    this.buildingService.getBuildingUsersTest().subscribe(
      data => {

         this.buildings = data.content;

    /*     const id = data.content[0].id;
        const name=data.content[0].name; */
        // local storage

       /*  this.buildingUpdateService.setIdBuilding(id);  */




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
