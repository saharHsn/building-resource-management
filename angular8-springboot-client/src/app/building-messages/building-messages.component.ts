import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {MessageService} from '../_services/message.service';
import {Message} from '../building/model/message';
import {AlertService} from '../_services';

@Component({
  selector: 'app-building-messages',
  templateUrl: './building-messages.component.html',
  styleUrls: ['./building-messages.component.css']
})
export class BuildingMessagesComponent implements OnInit {
  messages: Message[];
  options = {
    autoClose: false,
    keepAfterRouteChange: false
  };

  constructor(private route: ActivatedRoute,
              private messageService: MessageService,
              private alertService: AlertService) {
  }

  ngOnInit(): void {
    this.reloadData();
  }

  reloadData() {
    const buildingId = this.route.snapshot.params.buildingId;
    this.messageService.getBuildingMessageList(buildingId).subscribe(data => {
        this.messages = data.content;
        // this.alertService.success('Your dashboard will be rendered in a few minutes.\n', true);
      }
      , error => {
        console.log(error);
      });
  }

  deleteMessage(messageId: string) {
    this.alertService.clear();
    this.messageService.deleteMessage(messageId).subscribe(data => {
        this.alertService.success('Message was deleted successfully.\n', this.options);
        this.ngOnInit();
      }
      , error => {
        console.log(error);
      });
  }
}
