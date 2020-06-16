import {Component, OnInit} from '@angular/core';
import {MessageService} from 'src/app/_services/message.service';

@Component({
  selector: 'app-panel',
  templateUrl: './panel.component.html',
  styleUrls: ['./panel.component.css']
})
export class PanelComponent implements OnInit {
  listMessages: any[];

  constructor(private messageService: MessageService) {
  }

  ngOnInit() {
    this.init();
  }

  init() {
    this.messageService.getMessages().subscribe(data => {
      this.listMessages = data.content;
    });
  }
}
