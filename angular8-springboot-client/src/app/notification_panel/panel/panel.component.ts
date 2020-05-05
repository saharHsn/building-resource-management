import { Component, OnInit } from '@angular/core';
import { MessageService } from 'src/app/_services/message.service';
import { fadeInItems } from '@angular/material';

@Component({
  selector: 'app-panel',
  templateUrl: './panel.component.html',
  styleUrls: ['./panel.component.css']
})
export class PanelComponent implements OnInit {
listMessages:any[];
  constructor(private _message:MessageService) { }

  ngOnInit() {
this.init();
  
  }
init(){

  this._message.getMessages().subscribe(data=>{
    this.listMessages=data.content;
    console.log(this.listMessages);
  })

}
}
