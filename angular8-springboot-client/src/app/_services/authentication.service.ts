import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {User} from '../user/user';
import {environment} from '../../environments/environment';
import {LoginRequest} from '../user/LoginRequest';
import {ActivatedRoute, Router} from '@angular/router';


@Injectable({providedIn: 'root'})
export class AuthenticationService {
  private currentUserSubject: BehaviorSubject<User>;
  public currentUser: Observable<User>;
  returnUrl: string;

  environmentName = '';
  environmentUrl = 'Debug api';
  private readonly baseUrl;
  private loginRequest: LoginRequest;
  private headers: HttpHeaders;

  constructor(private http: HttpClient,
              private router: Router,
              private route: ActivatedRoute) {
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();

    this.environmentName = environment.environmentName;
    this.environmentUrl = environment.apiUrl;
  }

  public get currentUserValue(): User {
    if (this.currentUserSubject.value) {
      // @ts-ignore
      return this.currentUserSubject.value.content.user;
    }
    return null;
  }

  login(emailAddress, password) {
    this.loginRequest = new LoginRequest();
    this.loginRequest.password = password;
    this.loginRequest.email = emailAddress;
    return this.http.post<any>(`${this.environmentUrl}/users/authenticate/login`, this.loginRequest)
      .pipe(map(user => {
        // store user details and jwt token in local storage to keep user logged in between page refreshes
        localStorage.setItem('currentUser', JSON.stringify(user));
        this.currentUserSubject.next(user);
        return user;
      }));
  }

  logout() {
    // remove user from local storage and set current user to null
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    // this.router.navigate([this.route.snapshot.queryParams.returnUrl || '/']);
  }

  public getHeaders() {
    let headers;
    // @ts-ignore
    const user = this.currentUserValue.id ? this.currentUserValue : this.currentUserValue.content.user;
    if (user && user.token) {
      headers = new HttpHeaders()
        .set('X-Session', user.token)
        .set('Accept', '*/*')
        .set('Content-Type', 'application/json');
    }
    return headers;
  }

  /* sendInvitation(inviteeEmail: string, subject: string, message: string) {
     return this.http.post<any>(`${this.environmentUrl}/users/registration`, this.loginRequest)
       .pipe(map(user => {
       }));
   }*/

}
