import {Component, ViewChild} from '@angular/core';
import {NavigationEnd, Router} from '@angular/router';

import {AlertService, AuthenticationService} from './_services';
import {User} from './_models';
import {BsModalRef, BsModalService, ModalDirective} from 'ngx-bootstrap/modal';

import './_content/app.less';
import {DEFAULT_INTERRUPTSOURCES, Idle} from '@ng-idle/core';
import {Keepalive} from '@ng-idle/keepalive';
import {AppService} from './_services/app.service';
import {filter} from 'rxjs/operators';

// tslint:disable-next-line:ban-types
declare let gtag: Function;

@Component({selector: 'app-root', templateUrl: 'app.component.html'})
export class AppComponent {
  currentUser: User;
  sideBarOpen = true;
  idleState = 'Not started.';
  timedOut = false;
  lastPing?: Date = null;
  title = 'angular-idle-timeout';

  public modalRef: BsModalRef;

  @ViewChild('childModal', {static: false}) childModal: ModalDirective;
  private demoMood: boolean;

  constructor(private idle: Idle, private keepalive: Keepalive,
              private router: Router, private modalService: BsModalService,
              private authenticationService: AuthenticationService,
              private appService: AppService,
              public alertService: AlertService) {
    const navEndEvent$ = router.events.pipe(
      filter(e => e instanceof NavigationEnd)
    );
    navEndEvent$.subscribe((e: NavigationEnd) => {
      gtag('config', 'UA-159293470-1', {page_path: e.urlAfterRedirects});
    });

    // sets an idle timeout of 15 minutes, for testing purposes.
    idle.setIdle(60 * 60);
    // idle.setIdle(40);
    // sets a timeout period of 1 hour. after 10 seconds of inactivity, the user will be considered timed out.
    idle.setTimeout(60 * 60);
    // idle.setTimeout(40);
    // sets the default interrupts, in this case, things like clicks, scrolls, touches to the document
    idle.setInterrupts(DEFAULT_INTERRUPTSOURCES);
    idle.onIdleEnd.subscribe(() => {
      this.idleState = 'No longer idle.';
      // console.log(this.idleState);
      this.reset();
    });

    idle.onTimeout.subscribe(() => {
      this.childModal.hide();
      this.idleState = 'Timed out!';
      this.timedOut = true;
      // console.log(this.idleState);
      this.authenticationService.logout();
      this.router.navigate(['/login']);
    });

    idle.onIdleStart.subscribe(() => {
      this.idleState = 'You\'ve gone idle!';
      // console.log(this.idleState);
      this.childModal.show();
    });

    idle.onTimeoutWarning.subscribe((countdown) => {
      this.idleState = 'You will time out in ' + countdown + ' seconds!';
      // console.log(this.idleState);
    });

    // sets the ping interval to 15 seconds
    keepalive.interval(15);

    keepalive.onPing.subscribe(() => this.lastPing = new Date());

    this.router.events
      .subscribe(event => {
        if (event instanceof NavigationEnd) {
          const url = (event as NavigationEnd).url;
          if (url === '/demo') {
            this.demoMood = true;
            this.currentUser = null;
          } else {
            this.authenticationService.currentUser.subscribe(x => this.currentUser = x);
          }
        }
        this.appService.setUserLoggedIn(!!this.authenticationService.currentUserValue);
      });

    this.appService.getUserLoggedIn().subscribe(userLoggedIn => {
      if (userLoggedIn) {
        idle.watch();
        this.timedOut = false;
      } else {
        idle.stop();
      }
    });
  }

  reset() {
    this.idle.watch();
    // xthis.idleState = 'Started.';
    this.timedOut = false;
  }

  hideChildModal(): void {
    this.childModal.hide();
  }

  stay() {
    this.childModal.hide();
    this.reset();
  }

  logout() {
    // this.childModal.hide();
    this.appService.setUserLoggedIn(false);
    // this.router.navigate(['/']);
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }

  // open and close sidebar
  sideBarToggler() {
    this.sideBarOpen = !this.sideBarOpen;
  }
}
