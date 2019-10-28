import {RouterModule, Routes} from '@angular/router';

import {HomeComponent} from './home';
import {LoginComponent} from './login';
import {RegisterComponent} from './register';
import {AuthGuard} from './_helpers';
import {PredictionsComponent} from './chart/overall/predictions/predictions.component';
import {OverallComponent} from './chart/overall/overall.component';
import {BeScoreComponent} from './chart/overall/be-score/be-score.component';
import {CostComponent} from './chart/cost/cost.component';
import {CostStackChartComponent} from './chart/cost/cost-stack-chart/cost-stack-chart.component';
import {CostPieChartComponent} from './chart/cost/cost-pie-chart/cost-pie-chart.component';

const routes: Routes = [
  {path: '', component: HomeComponent, canActivate: [AuthGuard]},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  // { path: 'weather', component: WeatherComponent },
  {path: 'predictions', component: PredictionsComponent},
  {path: 'overall', component: OverallComponent},
  {path: 'be_score', component: BeScoreComponent},
  {path: 'cost', component: CostComponent},
  {path: 'cost-stack', component: CostStackChartComponent},
  {path: 'cost-stack', component: CostPieChartComponent},

  // otherwise redirect to home
  {path: '**', redirectTo: ''}
];

// @ts-ignore
export const appRoutingModule = RouterModule.forRoot(routes);
