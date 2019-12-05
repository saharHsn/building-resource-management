import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {Building} from '../building/model/building';

@Injectable({
  providedIn: 'root'
})
export class ChartService {
  environmentName = '';
  environmentUrl = 'Debug api';
  private readonly baseUrl;
  private readonly predictUrl;
  private readonly savingUrl;
  private readonly beScoreUrl;
  private readonly costStackUrl;
  private readonly costPieUrl;

  constructor(private http: HttpClient) {
    this.environmentName = environment.environmentName;
    this.environmentUrl = environment.apiUrl;
    this.baseUrl = this.environmentUrl + '/reports';
    this.predictUrl = this.baseUrl + '/prediction';
    this.savingUrl = this.baseUrl + '/saving';
    this.beScoreUrl = this.baseUrl + '/beScore';
    this.costStackUrl = this.baseUrl + '/costStack';
    this.costPieUrl = this.baseUrl + '/costPie';
  }

  predict(building: Building): Observable<any> {
    return this.http.get(`${this.predictUrl}/${33333333}`);
  }

  savingThisMonth(buildingId: string): Observable<any> {
    return this.http.get(`${this.savingUrl}/${33333333}`);
  }

  getBEScore(buildingId: any): Observable<any> {
    return this.http.get(`${this.beScoreUrl}/${33333333}`);
  }

  costStackData(buildingId: string): Observable<any> {
    return this.http.get(`${this.costStackUrl}/${33333333}`);
  }

  costPieData(buildingId: string): Observable<any> {
    return this.http.get(`${this.costPieUrl}/${33333333}`);
  }
}
