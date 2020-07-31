import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from './user';
import {environment} from '../../environments/environment';
import {AuthenticationService} from '../_services';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  environmentName = '';
  environmentUrl = 'Debug api';
  private baseUrl;
  headers: HttpHeaders;

  constructor(private http: HttpClient,
              private authService: AuthenticationService) {
    this.environmentName = environment.environmentName;
    this.environmentUrl = environment.apiUrl;
    this.baseUrl = this.environmentUrl + '/users';
    // this.headers = this.authService.getHeaders();
  }

  getUser(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  createUser(user: object): Observable<object> {
    return this.http.post(`${this.baseUrl}`, user);
  }

  register(user: User) {
    return this.http.post(`${this.baseUrl}/register`, user);
  }

  /* updateUser(id: number, value: any): Observable<object> {
     return this.http.put(`${this.baseUrl}/${id}`, value);
   }*/

  updateUser(changedUser: User): Observable<object> {
    let headers;
    // @ts-ignore
    const user = this.authService.currentUserValue.id ? this.authService.currentUserValue : this.authService.currentUserValue.content.user;
    if (user && user.token) {
      headers = new HttpHeaders()
        .set('X-Session', user.token)
        .set('Accept', '*/*')
        .set('Content-Type', 'application/json');
    }
    return this.http.put(`${this.baseUrl}`, changedUser, {headers});
  }

  deleteUser(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, {responseType: 'text'});
  }

  getUsersList(): Observable<any> {
    return this.http.get(`${this.baseUrl}`);
  }

  registerDemoUser(user: User, subscribe: boolean) {
    user.subscribe = subscribe;
    return this.http.post(`${this.baseUrl}/registerDemoUser`, user);
  }
}
