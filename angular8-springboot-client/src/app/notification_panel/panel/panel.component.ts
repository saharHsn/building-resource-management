import {Component, OnInit} from '@angular/core';
import {MessageService} from 'src/app/_services/message.service';

@Component({
  selector: 'app-panel',
  templateUrl: './panel.component.html',
  styleUrls: ['./panel.component.css']
})
export class PanelComponent implements OnInit {
  listMessages: any[];
  constructor(private _message: MessageService) { }

  ngOnInit() {
    this.init();
  }


  init() {

    this._message.getMessages().subscribe(data => {
      console.log(data.content);
      this.listMessages = data.content;
      console.log(this.listMessages);


    });

  }
}
