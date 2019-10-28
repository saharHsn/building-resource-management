import {RouterModule, Routes} from '@angular/router';

import {HomeComponent} from './home';
import {LoginComponent} from './login';
import {RegisterComponent} from './register';
import {AuthGuard} from './_helpers';
import {PredictionsComponent} from './chart/overall/predictions/predictions.component';
import {OverallComponent} from './chart/overall/overall.component';
import {BeScoreComponent} from './chart/overall/be-score/be-score.component';

const routes: Routes = [
    { path: '', component: HomeComponent, canActivate: [AuthGuard] },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
  // { path: 'weather', component: WeatherComponent },
  {path: 'predictions', component: PredictionsComponent},
  {path: 'overall', component: OverallComponent},
  {path: 'be_score', component: BeScoreComponent},

    // otherwise redirect to home
    { path: '**', redirectTo: '' }
];

export const appRoutingModule = RouterModule.forRoot(routes);
