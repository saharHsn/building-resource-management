import {Component, EventEmitter, Input, Output} from '@angular/core';
import {YearFilterType} from '../enum/YearFilterType';


@Component({
  selector: 'app-year-filter-component',
  template: `
      <p>
          <select [(ngModel)]="inputSliderValue" (change)="updateYearFilter()">
              <option *ngFor="let yearF of keys(year)" [ngValue]="yearF">
                  {{year[yearF]}}
              </option>
          </select>
      </p>
  `
})

export class YearFilterComponent {
  keys = Object.keys;
  year = YearFilterType;

  constructor() {
  }

  /**
   * Holds the current value of the slider
   */
    // @Input() inputSliderValue = '';
  @Input() inputSliderValue: YearFilterType = YearFilterType.YEAR_2019;

  /**
   * Invoked when the model has been changed
   */
  @Output() inputSliderValueChange: EventEmitter<string> = new EventEmitter<string>();

  updateYearFilter() {
    this.inputSliderValueChange.emit(this.inputSliderValue);
  }
}
