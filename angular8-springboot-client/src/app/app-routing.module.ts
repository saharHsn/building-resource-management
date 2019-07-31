import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {WizardFormComponent} from './wizard-form/wizard-form.component';
import {CreateUserComponent} from './user/create-user/create-user.component';
import {CreateBuildingComponent} from './building/create-building/create-building.component';

const routes: Routes = [
  {path: 'wizard', component: WizardFormComponent},
  {path: 'add-user', component: CreateUserComponent},
  {path: 'add-building', component: CreateBuildingComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
