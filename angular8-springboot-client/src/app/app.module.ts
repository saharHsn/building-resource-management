import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {CommonModule} from '@angular/common';
import {appRoutingModule} from './app.routing';
import {JwtInterceptor} from './_helpers';
// tslint:disable-next-line:max-line-length
import {NgIdleKeepaliveModule} from '@ng-idle/keepalive'; // this includes the core NgIdleModule but includes keepalive providers for easy wireup
// optional, provides moment-style pipes for date formatting
import {ModalModule} from 'ngx-bootstrap/modal';
import {AppComponent} from './app.component';
import {HomeComponent} from './home';
import {LoginComponent} from './login';
import {RegisterComponent} from './register';
import {InvitationComponent} from './invitation';
import {WizardFormComponent} from './wizard-form/wizard-form.component';
import {ArchwizardModule} from 'angular-archwizard';
import {BuildingUsageComponent} from './building/enums/building-usage-component';
import {BuildingAgeComponent} from './building/enums/building-age-component';
import {EnergyCertificateComponent} from './building/enums/energy-certificate-component';
import {BuildingDetailsComponent} from './building/building-details/building-details.component';

import {OverallComponent} from './charts/overall/overall.component';
import {WeatherComponent} from './weather/weather.component';
import {CostComponent} from './charts/cost/cost.component';
import {ConsumptionComponent} from './charts/consumption/consumption.component';
import {PredictionsComponent} from './charts/overall/predictions/predictions.component';
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

import {BeBreakdownComponent} from './charts/report/be-breakdown/be-breakdown.component';
import {ReportBeScoreComponent} from './charts/report/be-score/report-be-score.component';
import {PercentileComponent} from './charts/report/percentile/percentile.component';
import {EnergyEfficiencyComponent} from './charts/report/energy-efficiency/energy-efficiency.component';
import {CarbonComponent} from './charts/report/carbon/carbon.component';
import {BeScoreComponent} from './charts/overall/be-score/be-score.component';
import {ReportComponent} from './charts/report/report.component';
// tslint:disable-next-line:max-line-length
import {EnergyEfficiencySymbolsComponent} from './charts/report/energy-efficiency/energy_efficiency_symbols/energy-efficiency-symbols.component';
import {TooltipModule} from 'ng2-tooltip-directive';
import {UserListComponent} from './user/user-list/user-list.component';
import {UserDetailsComponent} from './user/user-details/user-details.component';
import {BuildingListComponent} from './building/building-list/building-list.component';
import {CreateBuildingComponent} from './building/create-building/create-building.component';
import {CreateUserComponent} from './user/create-user/create-user.component';
import {HighchartsChartModule} from 'highcharts-angular';

import {GoogleAnalyticsService} from './_analytics/google-analytics.service';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HeaderComponent} from './default/header/header.component';
import {SidebarComponent} from './default/sidebar/sidebar.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';


import {
  MatBadgeModule,
  MatButtonModule,
  MatDatepickerModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatNativeDateModule,
  MatProgressBarModule,
  MatSelectModule,
  MatSidenavModule,
  MatTabsModule,
  MatToolbarModule
} from '@angular/material';
import {MatCardModule} from '@angular/material/card';
import {ColorEnergyComponent} from './charts/report/color-energy/color-energy.component';
import {GeneralComponent} from './charts/profile/general/general.component';
import {BuildingComponent} from './charts/profile/building/building.component';
import {ProfileViewComponent} from './charts/profile/profile-view/profile-view.component';
import {MatBottomSheetModule} from '@angular/material/bottom-sheet';
import {MatDialogModule} from '@angular/material/dialog';
import {MatMenuModule} from '@angular/material/menu';
import {PanelComponent} from './notification_panel/panel/panel.component';
import {DownloadComponent} from './building/download/download.component';

import {DailyConsumptionComponent} from './charts/consumption/daily-consumption/daily-consumption.component';
import {ResetPasswordComponent} from './login/reset-password/reset-password.component';
import {BulletChartComponent} from './charts/report/bullet-chart/bullet-chart.component';
import {DailyElectricityComponent} from './charts/cost/daily-electricity/daily-electricity.component';
import {RemoveUnderlinePipe} from './pipes/remove-underline.pipe';
import {AdminPanelComponent} from './admin/admin-panel/admin-panel.component';
import {BuildingDetailsAdminComponent} from './building/building-details-admin/building-details-admin.component';
import {BuildingMessagesComponent} from './building-messages/building-messages.component';
import {AlertModule} from './_services/_alert/alert.module';
import {BuildingButtonComponent} from './_components/building-button/building-button.component';
import {TestbulletComponent} from './charts/report/testbullet/testbullet.component';
import {HeatmapHourlyComponent} from './charts/consumption/heatmap-hourly/heatmap-hourly.component';
import {HeatmapDailyComponent} from './charts/consumption/heatmap-daily/heatmap-daily.component';
import {AverageSolarComponent} from './charts/average-solar/average-solar.component';
import {DemoComponent} from './demo/demo.component';
// Import FusionCharts library and chart modules


// Pass the fusioncharts library and chart modules

// tslint:disable-next-line:max-line-length


@NgModule({
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    appRoutingModule,
    NgIdleKeepaliveModule.forRoot(),

    FormsModule,
    ArchwizardModule,
    CommonModule,
    NgSelectModule,
    TooltipModule,
    HighchartsChartModule,
    ModalModule.forRoot(),

    BrowserAnimationsModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatSidenavModule,
    MatListModule,
    MatCardModule,
    MatBadgeModule,
    MatTabsModule,
    MatBottomSheetModule,
    MatDialogModule,
    MatFormFieldModule,
    MatMenuModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatInputModule,
    MatSelectModule,

    NgbModule,
    AlertModule,
   MatProgressBarModule,

  ],
  declarations: [

    AppComponent,
    HomeComponent,
    LoginComponent,
    AdminPanelComponent,
    BuildingDetailsAdminComponent,
    RegisterComponent,
    UserListComponent,
    UserDetailsComponent,
    CreateUserComponent,
    BuildingListComponent,
    CreateBuildingComponent,
    InvitationComponent,
    WizardFormComponent,
    BuildingUsageComponent,
    BuildingAgeComponent,
    EnergyCertificateComponent,
    PanelComponent,
    OverallComponent,
    BeScoreComponent,
    WeatherComponent,
    CostComponent,
    ConsumptionComponent,
    PredictionsComponent,
    SidebarComponent,
    HeaderComponent,
    ProfileViewComponent,
    BuildingComponent,
    GeneralComponent,
  /*   AlertComponent, */

    // HighchartsChartComponent,
    ReportBeScoreComponent,
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
    NormalizedConsumptionComponent,
    ReportComponent,
    BuildingDetailsComponent,
    BeBreakdownComponent,
    ReportBeScoreComponent,
    PercentileComponent,
    EnergyEfficiencyComponent,
    CarbonComponent,
    EnergyEfficiencySymbolsComponent,
    ColorEnergyComponent,
    DownloadComponent,
    DailyConsumptionComponent,
    DailyElectricityComponent,
    ResetPasswordComponent,
    BulletChartComponent,
    DailyElectricityComponent,
    RemoveUnderlinePipe,
    BuildingMessagesComponent,
    BuildingButtonComponent,
    TestbulletComponent,
    DemoComponent,
    HeatmapHourlyComponent,
    HeatmapDailyComponent,
    AverageSolarComponent],
  entryComponents: [DownloadComponent],
  providers: [
    GoogleAnalyticsService,
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    // {provide: HTTP_INTERCEPTORS, useClass: HttpErrorInterceptor, multi: true}

    // provider used to create fake backend
    MatDatepickerModule],
  bootstrap: [AppComponent]
})
export class AppModule {
}


