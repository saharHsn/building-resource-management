import {RouterModule, Routes} from '@angular/router';

import {HomeComponent} from './home';
import {LoginComponent} from './login';
import {RegisterComponent} from './register';
import {AuthGuard} from './_helpers';
import {PredictionsComponent} from './charts/overall/predictions/predictions.component';
import {OverallComponent} from './charts/overall/overall.component';
import {BeScoreComponent} from './charts/overall/be-score/be-score.component';
import {CostComponent} from './charts/cost/cost.component';
import {CostStackChartComponent} from './charts/cost/cost-stack-chart/cost-stack-chart.component';
import {CostPieChartComponent} from './charts/cost/cost-pie-chart/cost-pie-chart.component';
import {CarbonFootPrintComponent} from './charts/carbon-foot-print/carbon-foot-print.component';
// tslint:disable-next-line:max-line-length
import {ConsumptionComponent} from './charts/consumption/consumption.component';
import {WizardFormComponent} from './wizard-form/wizard-form.component';
import {NormalizedConsumptionComponent} from './charts/normalized-consumption/normalized-consumption.component';
import {BuildingDetailsComponent} from './building/building-details/building-details.component';
import {ReportComponent} from './charts/report/report.component';

const routes: Routes = [
  {path: '', component: HomeComponent, canActivate: [AuthGuard]},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'wizard', component: WizardFormComponent},
  {path: 'building-detail', component: BuildingDetailsComponent},
  // { path: 'weather', component: WeatherComponent },
  {path: 'predictions', component: PredictionsComponent},
  {path: 'overall', component: OverallComponent},
  {path: 'be_score', component: BeScoreComponent},
  {path: 'cost', component: CostComponent},
  {path: 'cost-stack', component: CostStackChartComponent},
  {path: 'cost-stack', component: CostPieChartComponent},
  {path: 'carbon-footprint', component: CarbonFootPrintComponent},
  {path: 'consumption-component', component: ConsumptionComponent},
  {path: 'normalized-consumption', component: NormalizedConsumptionComponent},
  {path: 'report', component: ReportComponent},

  // otherwise redirect to home
  {path: '**', redirectTo: ''}
];

// @ts-ignore
export const appRoutingModule = RouterModule.forRoot(routes);
