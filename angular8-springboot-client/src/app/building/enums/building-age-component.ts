import {Component, EventEmitter, Input, Output} from '@angular/core';
import {BuildingAge} from "./buildingAge"

@Component({
  selector: 'building-age-component',
  template: `
    <p>
      <!--Having the symbol as label and name as value:-->
      <select class="browser-default custom-select" [(ngModel)]="inputSliderValue"
              (ngModelChange)="inputSliderValueChange.emit(inputSliderValue)">
        <option *ngFor="let bAge of keys(buildingAge)" [ngValue]="bAge">{{buildingAge[bAge]}}</option>
      </select>
    </p>
  `
})
export class BuildingAgeComponent  {
  keys = Object.keys;
  buildingAge = BuildingAge;

  /**
   * Holds the current value of the slider
   */
  @Input() inputSliderValue: string = "";

  /**
   * Invoked when the model has been changed
   */
  @Output() inputSliderValueChange: EventEmitter<string> = new EventEmitter<string>();
}
