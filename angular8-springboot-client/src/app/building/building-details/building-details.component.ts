import {Building} from '../model/building';
import {Component, OnInit} from '@angular/core';
import {BuildingService} from '../service/building.service';
import {ActivatedRoute, Router} from '@angular/router';
import {BuildingAge} from '../enums/buildingAge';
/* services of dialog */
import {MatDialog} from '@angular/material';
import {DownloadComponent} from '../download/download.component';
import {ChartService} from 'src/app/charts/chartService';
import {CurrentBuildingService} from "../../_services/current-building.service";
import { MessageService } from '../../_services/message.service';
import { BuildingUpdateService } from '../../_services/building-update.service';

@Component({
  selector: 'app-building-details',
  templateUrl: './building-details.component.html',
  styleUrls: ['./building-details.component.css']
})
export class BuildingDetailsComponent implements OnInit {

  id: number;
  building: Building;
  monthSummary:any;
  constructor(private route: ActivatedRoute,
              private router: Router,
              private buildingService: BuildingService,
              private currentBuildingService: BuildingUpdateService,
              public dialog: MatDialog,
              private chartService: ChartService,
              private messages:MessageService) {
  }

  ngOnInit() {
    // this.building = new Building();
    this.id = this.route.snapshot.params.id;
    const currentBuildingId = this.currentBuildingService.getIdBuilding();
    this.buildingService.getBuilding(currentBuildingId)
      .subscribe(data => {
       
        this.building = data.content;
        if (this.building != null) {
          this.building.builtIn = BuildingAge[this.building.age];
        } else {
          this.building = new Building();
        }
      }, error => console.log(error));

      this.monthSummary=this.messages.getMonth();
  }


  openDialog() {
    this.dialog.open(DownloadComponent);
  }

  /* list() {
     this.router.navigate(['buildings']);
   }*/
  getBuiltIn()
    :
    string {
    const buildingAge = this.building.age;
    const currentYear = (new Date()).getFullYear();
    if (BuildingAge[buildingAge] === BuildingAge.LESS_THAN_5_YEARS) {
      return String((currentYear - 5));
    } else if (BuildingAge[buildingAge] === BuildingAge.BETWEEN_5_TO_10_YEARS) {
      return String((currentYear - 10));
    } else if (BuildingAge[buildingAge] === BuildingAge.BETWEEN_10_TO_15_YEARS) {
      return String((currentYear - 15));
    } else if (BuildingAge[buildingAge] === BuildingAge.MORE_THAN_15) {
      return 'before ' + (currentYear - 15);
    }
  }

  getCurrentDate()
    :
    string {
    const currentDate = new Date();
    return currentDate.toDateString();
  }

  download() {
    this.chartService.download()
      .subscribe(response => {
        this.downLoadFile(response, 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;');
      });
  }

  downLoadFile(data
                 :
                 any, type
                 :
                 string
  ) {
    const blob = new Blob([data], {type});
    // window.open(this.restUrl, "_blank");
    const url = window.URL.createObjectURL(blob);
    const pwa = window.open(url, '_blank');
    if (!pwa || pwa.closed || typeof pwa.closed === 'undefined') {
      alert('Please disable your Pop-up blocker and try again.');
    }
  }
}
