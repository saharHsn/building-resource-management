import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {Building} from '../building/model/building';
import {YearFilterType} from './consumption/consumption-average-tariff-cost/filter-form/enum/YearFilterType';
import {TimePeriodType} from './consumption/consumption-average-tariff-cost/filter-form/enum/TimePeriodType';
import {DatePartType} from './consumption/consumption-average-tariff-cost/filter-form/enum/DatePartType';

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
  private readonly consumptionUrl;
  private readonly consumptionDynamicUrl;
  private readonly normalizedConsumptionWeatherUrl;
  private readonly normalizedPerCapitaUrl;
  private readonly normalizedVsEEUrl;
  private readonly predictedWeatherVSRealUrl;
  private readonly carbonPieDataUrl;
  private readonly carbonSPLineUrl;
  private readonly energyConsumptionIndexUrl;
  private readonly nationalMedianUrl;
  private readonly propertyTargetUrl;

  constructor(private http: HttpClient) {
    this.environmentName = environment.environmentName;
    this.environmentUrl = environment.apiUrl;
    this.baseUrl = this.environmentUrl + '/reports';
    this.predictUrl = this.baseUrl + '/prediction';
    this.savingUrl = this.baseUrl + '/saving';
    this.beScoreUrl = this.baseUrl + '/beScore';
    this.costStackUrl = this.baseUrl + '/costStack';
    this.costPieUrl = this.baseUrl + '/costPie';
    this.consumptionUrl = this.baseUrl + '/consumption';
    this.consumptionDynamicUrl = this.baseUrl + '/consumptionDynamic';
    this.normalizedConsumptionWeatherUrl = this.baseUrl + '/normConsumptionWeather';
    this.normalizedPerCapitaUrl = this.baseUrl + '/normPerCapita';
    this.normalizedVsEEUrl = this.baseUrl + '/normVSEE';
    this.predictedWeatherVSRealUrl = this.baseUrl + '/predictedWeatherVSReal';
    this.carbonPieDataUrl = this.baseUrl + '/carbonPie';
    this.carbonSPLineUrl = this.baseUrl + '/carbonSPLine';
    this.energyConsumptionIndexUrl = this.baseUrl + '/energyConsumptionIndex';
    this.nationalMedianUrl = this.baseUrl + '/nationalMedian';
    this.propertyTargetUrl = this.baseUrl + '/propertyTarget';
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

  consumptionStackData(buildingId: string): Observable<any> {
    return this.http.get(`${this.consumptionUrl}/${33333333}`);
  }

  consumptionDynamicData(buildingId: string,
                         year: YearFilterType,
                         timePeriod: TimePeriodType,
                         datePartType: DatePartType): Observable<any> {
    const params = new HttpParams()
      .set('year', year)
      .set('periodType', timePeriod)
      .set('datePartType', datePartType);
    return this.http.get(`${this.consumptionDynamicUrl}/${33333333}`,
      {params}
    );
  }

  normalizedConsumptionWeatherData(buildingId: string): Observable<any> {
    return this.http.get(`${this.normalizedConsumptionWeatherUrl}/${33333333}`);
  }

  normalizedPerCapitaData(buildingId: string): Observable<any> {
    return this.http.get(`${this.normalizedPerCapitaUrl}/${33333333}`);
  }

  normalizedVsEnergyEfficiency(buildingId: string): Observable<any> {
    return this.http.get(`${this.normalizedVsEEUrl}/${33333333}`);
  }

  predictedWeatherVSReal(buildingId: string): Observable<any> {
    return this.http.get(`${this.predictedWeatherVSRealUrl}/${33333333}`);
  }

  carbonPieData(buildingId: string): Observable<any> {
    return this.http.get(`${this.carbonPieDataUrl}/${33333333}`);
  }

  carbonSPLineData(buildingId: string): Observable<any> {
    return this.http.get(`${this.carbonSPLineUrl}/${33333333}`);
  }

  getAllEnergyConsumptionIndexes(buildingId: number): Observable<any> {
    return this.http.get(`${this.energyConsumptionIndexUrl}/${33333333}`);
  }

  getNationalMedian(buildingId: number): Observable<any> {
    return this.http.get(`${this.nationalMedianUrl}/${33333333}`);
  }

  getPropertyTarget(buildingId: number): Observable<any> {
    return this.http.get(`${this.propertyTargetUrl}/${33333333}`);
  }
}
