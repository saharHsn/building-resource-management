import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule, HTTP_INTERCEPTORS} from '@angular/common/http';


import {appRoutingModule} from './app.routing';
import {JwtInterceptor} from './_helpers';
import {AppComponent} from './app.component';
import {HomeComponent} from './home';
import {LoginComponent} from './login';
import {RegisterComponent} from './register';
import {AlertComponent} from './_components';
import {WizardFormComponent} from './wizard-form/wizard-form.component';
import {ArchwizardModule} from "angular-archwizard";
import {BuildingUsageComponent} from "./building/enums/building-usage-component";
import {BuildingAgeComponent} from "./building/enums/building-age-component";
import {EnergyCertificateComponent} from "./building/enums/energy-certificate-component";

@NgModule({
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    appRoutingModule,
    FormsModule,
    ArchwizardModule
  ],
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    AlertComponent,
    WizardFormComponent,
    BuildingUsageComponent,
    BuildingAgeComponent,
    EnergyCertificateComponent
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},

    // provider used to create fake backend
    //fakeBackendProvider
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
};
