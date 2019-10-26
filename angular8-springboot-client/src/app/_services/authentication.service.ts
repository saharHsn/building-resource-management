import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {environment} from 'src/environments/environment';
import {User} from '../user/user';
import {LoginRequest} from "../user/LoginRequest";
import {InvitationRequest} from "../register/InvitationRequest";

@Injectable({providedIn: 'root'})
export class AuthenticationService {
  private currentUserSubject: BehaviorSubject<User>;
  environmentName = '';
  environmentUrl = 'Debug api';
  public currentUser: Observable<User>;
  private loginRequest: LoginRequest;
  private invitationRequest: InvitationRequest;

  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
    this.environmentName = environment.environmentName;
    this.environmentUrl = environment.apiUrl;
  }

  public get currentUserValue(): User {
    return this.currentUserSubject.value;
  }

  login(emailAddress, password) {
    this.loginRequest = new LoginRequest();
    this.loginRequest.email = emailAddress;
    this.loginRequest.password = password;

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
  }

  sendInvitation(inviteeEmail: String, subject: String, message: String) {
    return this.http.post<any>(`${this.environmentUrl}/users/registration`, this.loginRequest)
      .pipe(map(user => {
      }));
  }
}
