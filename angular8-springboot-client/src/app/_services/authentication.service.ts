import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {environment} from 'src/environments/environment';
import {User} from '../user/user';
import {LoginRequest} from '../user/LoginRequest';
import {InvitationRequest} from '../register/InvitationRequest';
import {UserValue} from './UserValue';

@Injectable({providedIn: 'root'})
export class AuthenticationService {
  private currentUserSubject: BehaviorSubject<UserValue>;
  environmentName = '';
  environmentUrl = 'Debug api';
  public currentUser: Observable<UserValue>;
  private loginRequest: LoginRequest;
  private invitationRequest: InvitationRequest;
  private userValue: UserValue;

  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<UserValue>(JSON.parse(localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
    this.environmentName = environment.environmentName;
    this.environmentUrl = environment.apiUrl;
  }

  /* public getCurrentUserValue(): UserValue {
     return this.currentUserSubject.value;
   }
 */
  public getCurrentUser(): User {
    if (this.currentUserSubject.value) {
      return this.currentUserSubject.value.user;
    } else {
      return null;
    }
  }

  login(emailAddress, password) {
    this.loginRequest = new LoginRequest();
    this.loginRequest.email = emailAddress;
    this.loginRequest.password = password;

    return this.http.post<any>(`${this.environmentUrl}/users/authenticate/login`, this.loginRequest)
      .pipe(map(user => {
        // store user details and jwt token in local storage to keep user logged in between page refreshes
        localStorage.setItem('currentUser', JSON.stringify(user.content));
        this.currentUserSubject.next(user.content);
        return user.content;
      }));
  }

  logout() {
    // remove user from local storage and set current user to null
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }

  sendInvitation(inviteeEmail: string, subject: string, message: string) {
    return this.http.post<any>(`${this.environmentUrl}/users/registration`, this.loginRequest)
      .pipe(map(user => {
      }));
  }

  public getHeaders() {
    let headers;
    const user = this.getCurrentUser();
    if (user && user.token) {
      headers = new HttpHeaders()
        .set('X-Session', user.token)
        .set('Accept', '*/*')
        .set('Content-Type', 'application/json');
    }
    return headers;
  }
}
