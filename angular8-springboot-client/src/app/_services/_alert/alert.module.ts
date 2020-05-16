import {CommonModule} from '@angular/common';
import {AlertComponent} from './alert.component';
import {NgModule} from '@angular/core';

@NgModule({
  imports: [CommonModule],
  declarations: [AlertComponent],
  exports: [AlertComponent]
})
export class AlertModule {
}
