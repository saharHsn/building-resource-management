import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {CreateUserComponent} from './user/create-user/create-user.component';
import {UserDetailsComponent} from './user/user-details/user-details.component';
import {UserListComponent} from './user/user-list/user-list.component';
import {CreateBuildingComponent} from './building/create-building/create-building.component';
import {BuildingDetailsComponent} from './building/building-details/building-details.component';
import {BuildingListComponent} from './building/building-list/building-list.component';
import {BuildingAgeComponent} from "./building/enums/building-age-component";
import {BuildingUsageComponent} from "./building/enums/building-usage-component";
import {EnergyCertificateComponent} from "./building/enums/energy-certificate-component";
import {WizardFormComponent} from './wizard-form/wizard-form.component';
import {HttpClientModule} from '@angular/common/http';
import { TooltipModule } from 'ng2-tooltip-directive';

import {ArchwizardModule} from 'angular-archwizard';


@NgModule({
  declarations: [
    AppComponent,
    CreateUserComponent,
    UserDetailsComponent,
    UserListComponent,
    CreateBuildingComponent,
    BuildingDetailsComponent,
    BuildingListComponent,
    BuildingAgeComponent,
    EnergyCertificateComponent,
    WizardFormComponent,
    BuildingUsageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ArchwizardModule,
    TooltipModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
