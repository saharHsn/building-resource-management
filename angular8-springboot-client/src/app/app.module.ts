import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {CommonModule} from '@angular/common';
// import { AngularWeatherWidgetModule, WeatherApiName } from 'angular-weather-widget';
import {HighchartsChartComponent} from 'highcharts-angular';


import {appRoutingModule} from './app.routing';
import {JwtInterceptor} from './_helpers';
import {AppComponent} from './app.component';
import {HomeComponent} from './home';
import {LoginComponent} from './login';
import {RegisterComponent} from './register';
import {InvitationComponent} from './invitation';
import {AlertComponent} from './_components';
import {WizardFormComponent} from './wizard-form/wizard-form.component';
import {ArchwizardModule} from 'angular-archwizard';
import {BuildingUsageComponent} from './building/enums/building-usage-component';
import {BuildingAgeComponent} from './building/enums/building-age-component';
import {EnergyCertificateComponent} from './building/enums/energy-certificate-component';

import {OverallComponent} from './chart/overall/overall.component';
import {WeatherComponent} from './weather/weather.component';
import {CostComponent} from './chart/cost/cost.component';
import {ConsumptionComponent} from './chart/consumption/consumption.component';
import {PredictionsComponent} from './chart/overall/predictions/predictions.component';
import {BeScoreComponent} from './chart/overall/be-score/be-score.component';
import {CostPieChartComponent} from './chart/cost/cost-pie-chart/cost-pie-chart.component';
import {CostStackChartComponent} from './chart/cost/cost-stack-chart/cost-stack-chart.component'

@NgModule({
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    appRoutingModule,
    FormsModule,
    ArchwizardModule,
    CommonModule
  ],
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    InvitationComponent,
    AlertComponent,
    WizardFormComponent,
    BuildingUsageComponent,
    BuildingAgeComponent,
    EnergyCertificateComponent,
    OverallComponent,
    WeatherComponent,
    CostComponent,
    ConsumptionComponent,
    PredictionsComponent,
    HighchartsChartComponent,
    BeScoreComponent
    ,
    CostPieChartComponent,
    CostStackChartComponent,
    CostStackChartComponent
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    // {provide: HTTP_INTERCEPTORS, useClass: HttpErrorInterceptor, multi: true}

    // provider used to create fake backend
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
