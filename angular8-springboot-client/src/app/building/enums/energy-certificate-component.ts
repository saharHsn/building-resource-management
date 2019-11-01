import {Component, EventEmitter, Input, Output} from '@angular/core';
import {EnergyCertificate} from './energyCertificate';

@Component({
  selector: 'app-energy-certificate-component',
  template: `
    <p>
      <!--Having the symbol as label and name as value:-->
      <select class="browser-default custom-select" [(ngModel)]="inputSliderValue"
              (ngModelChange)="inputSliderValueChange.emit(inputSliderValue)">
        <option *ngFor="let energyCert of keys(energyCertificate)"
                [ngValue]="energyCert">{{energyCertificate[energyCert]}}</option>
      </select>
    </p>
  `
})
export class EnergyCertificateComponent  {
  keys = Object.keys;
  energyCertificate = EnergyCertificate;

  /**
   * Holds the current value of the slider
   */
  @Input() inputSliderValue = '';

  /**
   * Invoked when the model has been changed
   */
  @Output() inputSliderValueChange: EventEmitter<string> = new EventEmitter<string>();
}
