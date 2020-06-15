import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {AlertService, AuthenticationService} from '../_services';
import {UserService} from '../user/user.service';
import {first} from 'rxjs/operators';
import {AppService} from '../_services/app.service';
import {BuildingService} from '../building/service/building.service';
import {BuildingUpdateService} from '../_services/building-update.service';

@Component({
  selector: 'app-demo',
  templateUrl: './demo.component.html',
  styleUrls: ['./demo.component.css']
})
export class DemoComponent implements OnInit {
  demoForm: FormGroup;
  loading = false;
  submitted = false;
  checked = false;
  subscribeList: any = [
    {id: 1, name: 'I would like to receive information and offers from Builtrix'}
  ];

  constructor(private formBuilder: FormBuilder,
              private router: Router,
              private authenticationService: AuthenticationService,
              private userService: UserService,
              private route: ActivatedRoute,
              private appService: AppService,
              private alertService: AlertService,
              private buildingService: BuildingService,
              private buildingUpdateService: BuildingUpdateService) {
  }

  ngOnInit(): void {
    this.demoForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      emailAddress: ['', Validators.required],
      jobTitle: ['', Validators.required],
      interest: ['', null],
      subscription: ['', null]
    });
  }

  get f() {
    return this.demoForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.demoForm.invalid) {
      return;
    }

    this.loading = true;
    this.authenticationService.login('metrics_demo@builtrix.com', '1qazxsW@')
      .pipe(first())
      .subscribe(
        data => {
          this.appService.setUserLoggedIn(true);
          // bring the buildings
          this.buildingService.getBuildingUsersTest().subscribe(
            // tslint:disable-next-line:no-shadowed-variable
            data => {
              /*  this.buildings = data.content; */
              const id = data.content[0].id;
              const name = data.content[0].name;

              this.buildingUpdateService.setIdBuilding(id);
              this.userService.registerDemoUser(this.demoForm.value, this.checked)
                .pipe(first())
                .subscribe(
                  data1 => {
                    this.alertService.success('You registered successfully.', true);
                  },
                  error => {
                    let errorMessage = 'Unknown Error (Internal server error!)';
                    if (error.status === 400 && error.error.errors) {
                      errorMessage = error.error.errors;
                    } else {
                      errorMessage = error.error.message;
                    }
                    this.alertService.error(errorMessage);
                    this.loading = false;
                  });
              this.router.navigate(['/overall']);
            },
            error => console.log(error)
          );
        },
        error => {
          this.alertService.error(error.error.message);
          this.loading = false;
        });
  }

  onCheckboxChange(e) {
    const subs: FormArray = this.demoForm.get('subscription') as FormArray;
    if (e.target.checked) {
      // subs.push(new FormControl(e.target.value));
      // this.f.subscription.setValue(true);
      this.checked = true;
    } else {
      const index = subs.controls.findIndex(x => x.value === e.target.value);
      // subs.removeAt(index);
      this.checked = false;
    }
  }
}
