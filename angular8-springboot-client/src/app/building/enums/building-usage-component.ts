import {Component, EventEmitter, Input, Output} from '@angular/core';

import {BuildingUsage} from "./buildingUsage";

@Component({
  selector: 'building-usage-component',
  template: `    
    <p>
      <!--Having the symbol as label and name as value:-->
      <select class="browser-default custom-select" [(ngModel)]="inputSliderValue"
              (ngModelChange)="inputSliderValueChange.emit(inputSliderValue)">
        <option *ngFor="let bUsage of keys(buildingUsage)" [ngValue]="bUsage">{{buildingUsage[bUsage]}}</option>
      </select>
    </p>
  `
})

//[(ngModel)]="inputSliderValue" (ngModelChange)="inputSliderValueChange.emit(inputSliderValue)"
//[(ngModel)]="value" (ngModelChange)="updateChanges()"
export class BuildingUsageComponent  {
  keys = Object.keys;
  buildingUsage = BuildingUsage;

  /**
   * Holds the current value of the slider
   */
  @Input() inputSliderValue: string = "";

  /**
   * Invoked when the model has been changed
   */
  @Output() inputSliderValueChange: EventEmitter<string> = new EventEmitter<string>();


}
