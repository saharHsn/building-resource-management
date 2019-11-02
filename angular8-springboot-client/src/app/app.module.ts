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

import {OverallComponent} from './charts/overall/overall.component';
import {WeatherComponent} from './weather/weather.component';
import {CostComponent} from './charts/cost/cost.component';
import {ConsumptionComponent} from './charts/consumption/consumption.component';
import {PredictionsComponent} from './charts/overall/predictions/predictions.component';
import {BeScoreComponent} from './charts/overall/be-score/be-score.component';
import {CostPieChartComponent} from './charts/cost/cost-pie-chart/cost-pie-chart.component';
import {CostStackChartComponent} from './charts/cost/cost-stack-chart/cost-stack-chart.component';
import {CarbonFootPrintComponent} from './charts/carbon-foot-print/carbon-foot-print.component';
import {CarbonSplineChartComponent} from './charts/carbon-foot-print/carbon-spline-chart/carbon-spline-chart.component';
import {CarbonPieChartComponent} from './charts/carbon-foot-print/carbon-pie-chart/carbon-pie-chart.component';
// tslint:disable-next-line:max-line-length
import {ConsumptionDynamicBarChartComponent} from './charts/consumption/consumption-average-tariff-cost/consumption-dynamic-bar-chart/consumption-dynamic-bar-chart.component';
// tslint:disable-next-line:max-line-length
import {AverageTariffFilterComponent} from './charts/consumption/consumption-average-tariff-cost/filter-form/average-tariff-filter-component';
import {YearFilterComponent} from './charts/consumption/consumption-average-tariff-cost/filter-form/enum-components/year-filter-component';
import {DatePartComponent} from './charts/consumption/consumption-average-tariff-cost/filter-form/enum-components/date-part-component';
import {TimePeriodComponent} from './charts/consumption/consumption-average-tariff-cost/filter-form/enum-components/time-period-component';
import {NgSelectModule} from '@ng-select/ng-select';
// tslint:disable-next-line:max-line-length
import {ElectricityConsumptionOverTimeComponent} from './charts/consumption/electricity-consumption-over-time/electricity-consumption-over-time.component';
// tslint:disable-next-line:max-line-length
import {NormalizedVsEnergyEfficiencyComponent} from './charts/normalized-consumption/normalized-vs-energy-efficiency/normalized-vs-energy-efficiency.component';
import {ConsumptionWeatherComponent} from './charts/normalized-consumption/consumption-weather/consumption-weather.component';
// tslint:disable-next-line:max-line-length
import {PredictedWeatherVsRealComponent} from './charts/normalized-consumption/predicted-weather-vs-real/predicted-weather-vs-real.component';
import {NormalizedPerCapitaComponent} from './charts/normalized-consumption/normalized-per-capita/normalized-per-capita.component';
import {NormalizedConsumptionComponent} from './charts/normalized-consumption/normalized-consumption.component';

@NgModule({
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    appRoutingModule,
    FormsModule,
    ArchwizardModule,
    CommonModule,
    NgSelectModule,
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
    BeScoreComponent,
    CostPieChartComponent,
    CostStackChartComponent,
    CostStackChartComponent,
    CarbonFootPrintComponent,
    CarbonSplineChartComponent,
    CarbonPieChartComponent,
    ConsumptionDynamicBarChartComponent,
    AverageTariffFilterComponent,
    YearFilterComponent,
    DatePartComponent,
    TimePeriodComponent,
    ElectricityConsumptionOverTimeComponent,
    NormalizedPerCapitaComponent,
    PredictedWeatherVsRealComponent,
    ConsumptionWeatherComponent,
    NormalizedVsEnergyEfficiencyComponent,
    NormalizedConsumptionComponent],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    // {provide: HTTP_INTERCEPTORS, useClass: HttpErrorInterceptor, multi: true}

    // provider used to create fake backend
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
