import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Building} from '../model/building';
import {BuildingService} from '../service/building.service';
import {BuildingAge} from '../enums/buildingAge';
import {User} from '../../user/user';
import {AlertService} from '../../_services';
import {ModalDismissReasons, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {MessageService} from '../../_services/message.service';
import {BuildingMessagesComponent} from '../../building-messages/building-messages.component';

@Component({
  selector: 'app-building-details-admin',
  templateUrl: './building-details-admin.component.html',
  styleUrls: ['./building-details-admin.component.css']
})
export class BuildingDetailsAdminComponent implements OnInit {
  @ViewChild(BuildingMessagesComponent, {static: false}) buildingMessagesComponent: BuildingMessagesComponent;
  options = {
    autoClose: false,
    keepAfterRouteChange: false
  };

  constructor(private route: ActivatedRoute,
              private buildingService: BuildingService,
              private alertService: AlertService,
              private modalService: NgbModal,
              private messageService: MessageService
  ) {
  }

  building: Building;
  user: User;
  submitted = false;
  loading = false;
  messageBody: string;

  @ViewChild('electricityFile', {static: false})
  electricityFileVariable: ElementRef;

  closeResult: string;

  private static getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  ngOnInit() {
    const buildingId = this.route.snapshot.params.buildingId;
    this.buildingService.getBuilding(buildingId).subscribe(data => {
      this.building = data.content;
      console.log(this.building.owner);
      if (this.building != null) {
        this.building.builtIn = BuildingAge[this.building.age];
      } else {
        this.building = new Building();
      }
    }, error => console.log(error));
  }

  deleteBuildingBills() {
    this.loading = true;
    this.buildingService.deleteAllBills(this.building.id).subscribe(
      data => {
        this.alertService.success('All bills of this building were deleted  successfully.', true);
        this.loading = false;
      },
      error => {
        let errorMessage = 'Unknown Error (Internal server error!)';
        if (error.status === 400 && error.error.errors) {
          errorMessage = error.error.errors;
        } else {
          errorMessage = error.error.message;
        }
        this.alertService.error(errorMessage);
        this.loading = false;
      });
  }

  open(content) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      this.messageService.createNewMessage(this.messageBody, this.building.id).subscribe(
        data => {
          this.alertService.success('Message was created successfully.', this.options);
          this.buildingMessagesComponent.ngOnInit();
          this.loading = false;
          this.messageBody = '';
        },
        error => {
          let errorMessage = 'Unknown Error (Internal server error!)';
          if (error.status === 400 && error.error.errors) {
            errorMessage = error.error.errors;
          } else {
            errorMessage = error.error.message;
          }
          this.alertService.error(errorMessage);
          this.loading = false;
        }
      );
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${BuildingDetailsAdminComponent.getDismissReason(reason)}`;
    });
  }
}

