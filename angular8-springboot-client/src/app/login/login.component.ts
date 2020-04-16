import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {first} from 'rxjs/operators';

import {AlertService, AuthenticationService} from '../_services';
import {AppService} from '../_services/app.service';
import {GoogleAnalyticsService} from '../_analytics/google-analytics.service';

@Component({templateUrl: 'login.component.html'})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  loading = false;
  submitted = false;
  returnUrl: string;
  passwordPage=false;
  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    private appService: AppService,
    private alertService: AlertService,
    private googleAnalyticsService: GoogleAnalyticsService
  ) {
    // redirect to home if already logged in
    /*if (this.authenticationService.currentUserValue) {
      this.router.navigate(['/wizard']);
    }*/
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      emailAddress: ['', Validators.required],
      password: ['', Validators.required]
    });

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/wizard';
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.authenticationService.login(this.f.emailAddress.value, this.f.password.value)
      .pipe(first())
      .subscribe(
        data => {
          this.googleAnalyticsService.eventEmitter('user-login', 'user', 'login', 'login', 10);
          // this.router.navigate([this.returnUrl]);
          this.appService.setUserLoggedIn(true);
          this.router.navigate(['/overall']);
        },
        error => {
          this.alertService.error(error.error.message);
          this.loading = false;
        });
  }
}
