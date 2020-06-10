import { Injectable, EventEmitter } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import { Observable, Subscription, Subject } from 'rxjs';
import {environment} from '../../environments/environment';
import {AuthenticationService} from '../_services';
import {CurrentBuildingService} from './current-building.service';
import {BuildingUpdateService} from '../_services/building-update.service';
import { CurrentMonthSummary } from '../charts/overall/CurrentMonthSummary';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  private _listners = new Subject<any>();
  month:number;
  environmentName = '';
  environmentUrl = 'Debug api';
  private readonly baseUrl;
  invokeFirstComponentFunction = new EventEmitter();    
  subsVar: Subscription; 
  constructor(private http: HttpClient,
              private authService: AuthenticationService, 
              private buildingUpdateService: BuildingUpdateService) {

    this.environmentName = environment.environmentName;
    this.environmentUrl = environment.apiUrl;
    this.baseUrl = this.environmentUrl + '/messages';
    this.month= new Date().getMonth()+1;
    
  }
 




  private callService(restUrl: string) {
    let headers;
    // @ts-ignore
    const user = this.authService.currentUserValue.id ? this.authService.currentUserValue : this.authService.currentUserValue.content.user;
    if (user && user.token) {
      headers = new HttpHeaders()
        .set('X-Session', user.token)
        .set('Accept', '*/*')
        .set('Content-Type', 'application/json');
    }
    return this.http.get(`${restUrl}`, {headers});
  }

  getMessages(): Observable<any> {
    let currentBuildingId = this.buildingUpdateService.getIdBuilding();
    console.log(`mensajes llamados desde getmessages ${currentBuildingId}`)
    return this.callService(`${this.baseUrl}/${currentBuildingId}`);
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

  getBuildingMessageList(buildingId: string): Observable<any> {
    return this.callService(`${this.baseUrl}/${buildingId}`);
  }

  deleteMessage(messageId: string): Observable<any> {
    let headers;
    // @ts-ignore
    const user = this.authService.currentUserValue.id ? this.authService.currentUserValue : this.authService.currentUserValue.content.user;
    if (user && user.token) {
      headers = new HttpHeaders()
        .set('X-Session', user.token)
        .set('Accept', '*/*')
        .set('Content-Type', 'application/json');
    }
     return this.http.delete(`${this.baseUrl}/${messageId}`, {headers}); 
    
  }

  createNewMessage(messageBody: string, buildingId: string) {
    let headers;
    // @ts-ignore
    const user = this.authService.currentUserValue.id ? this.authService.currentUserValue : this.authService.currentUserValue.content.user;
    if (user && user.token) {
      headers = new HttpHeaders()
        .set('X-Session', user.token)
        .set('Accept', '*/*')
        .set('Content-Type', 'application/json');
    }
    return this.http.post(`${this.baseUrl}/${buildingId}`, messageBody, {headers});
  }


  getMonth(){
    

    const months = [
      {value: 1, viewValue: 'January'},
      {value: 2, viewValue: 'February'},
      {value: 3, viewValue: 'March'},
      {value: 4, viewValue: 'April'},
      {value: 5, viewValue: 'May'},
      {value: 6, viewValue: 'June'},
      {value: 7, viewValue: 'July'},
      {value: 8, viewValue: 'August'},
      {value: 9, viewValue: 'September'},
      {value: 10, viewValue: 'October'},
      {value: 11, viewValue: 'November'},
      {value: 12, viewValue: 'December'},
    ];

      const  currentMonth=months.filter(data=>{
        return data.value===this.month;

      })
      console.log(currentMonth);
        return currentMonth;

  }
//this return an observable 
  listen():Observable<any>{
  return  this._listners.asObservable();
  }
  activeMessage() {
    this._listners.next();
 }

}
