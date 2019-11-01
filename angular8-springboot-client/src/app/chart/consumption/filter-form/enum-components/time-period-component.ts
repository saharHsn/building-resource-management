import {Component, EventEmitter, Input, Output} from '@angular/core';
import {TimePeriodType} from '../enum/TimePeriodType';


@Component({
  selector: 'app-time-period-component',
  template: `
      <p>
          <select [(ngModel)]="inputSliderValue" (change)="updateTimePeriodFilter()">
              <option *ngFor="let timeP of keys(timePeriodType)" [ngValue]="timeP">{{timePeriodType[timeP]}}</option>
          </select>
      </p>
  `
})

// [(ngModel)]="inputSliderValue" (ngModelChange)="inputSliderValueChange.emit(inputSliderValue)"
// [(ngModel)]="value" (ngModelChange)="updateChanges()"
export class TimePeriodComponent {
  keys = Object.keys;
  timePeriodType = TimePeriodType;

  /**
   * Holds the current value of the slider
   */
  @Input() inputSliderValue = '';

  /**
   * Invoked when the model has been changed
   */
  @Output() inputSliderValueChange: EventEmitter<string> = new EventEmitter<string>();

  updateTimePeriodFilter() {
    this.inputSliderValueChange.emit(this.inputSliderValue);
  }
}
