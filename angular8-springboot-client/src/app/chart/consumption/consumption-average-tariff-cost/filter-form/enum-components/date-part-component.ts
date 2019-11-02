import {Component, EventEmitter, Input, Output} from '@angular/core';
import {DatePartType} from '../enum/DatePartType';


@Component({
  selector: 'app-date-part-component',
  template: `
      <p>
          <select [(ngModel)]="inputSliderValue" (change)="updateDatePartFilter()">
              <option *ngFor="let dateP of keys(datePartType)" [ngValue]="dateP">{{datePartType[dateP]}}</option>
          </select>
      </p>
  `
})

export class DatePartComponent {
  keys = Object.keys;
  datePartType = DatePartType;

  /**
   * Holds the current value of the slider
   */
  @Input() inputSliderValue = '';

  /**
   * Invoked when the model has been changed
   */
  @Output() inputSliderValueChange: EventEmitter<string> = new EventEmitter<string>();

  updateDatePartFilter() {
    this.inputSliderValueChange.emit(this.inputSliderValue);
  }
}
