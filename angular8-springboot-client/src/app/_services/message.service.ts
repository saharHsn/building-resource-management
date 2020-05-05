import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {AuthenticationService} from '../_services';
import {BuildingUpdateService} from './building-update.service';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  environmentName = '';
  environmentUrl = 'Debug api';
  private readonly baseUrl;

  constructor(private http: HttpClient,
              private authService: AuthenticationService, private buildingUpdateService: BuildingUpdateService) {

    this.environmentName = environment.environmentName;
    this.environmentUrl = environment.apiUrl;
    this.baseUrl = this.environmentUrl + '/messages';
  }

  private callService(restUrl: string) {
    const idcurrentBuilding = this.buildingUpdateService.getIdBuilding();
    let headers;
    // @ts-ignore
    const user = this.authService.currentUserValue.id ? this.authService.currentUserValue : this.authService.currentUserValue.content.user;
    if (user && user.token) {
      headers = new HttpHeaders()
        .set('X-Session', user.token)
        .set('Accept', '*/*')
        .set('Content-Type', 'application/json');
    }
    return this.http.get(`${restUrl}/${idcurrentBuilding}`, {headers});
  }

  getMessages(): Observable<any> {
    return this.callService(`${this.baseUrl}`);
  }

  readMessages(idMessage) {
    let headers;
    const message = idMessage;

    // @ts-ignore
    const user = this.authService.currentUserValue.id ? this.authService.currentUserValue : this.authService.currentUserValue.content.user;
    if (user && user.token) {
      headers = new HttpHeaders()
        .set('X-Session', user.token)
        .set('Accept', '*/*');
    }
    const params = new HttpParams()
      .set('messageId', message)
      .set('readStatus', 'true');
    return this.http.post(`${this.baseUrl}/updateReadStatus`,
      params, {headers});
  }
}
