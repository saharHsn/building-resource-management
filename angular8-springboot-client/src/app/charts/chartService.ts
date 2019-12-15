import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {YearFilterType} from './consumption/consumption-average-tariff-cost/filter-form/enum/YearFilterType';
import {TimePeriodType} from './consumption/consumption-average-tariff-cost/filter-form/enum/TimePeriodType';
import {DatePartType} from './consumption/consumption-average-tariff-cost/filter-form/enum/DatePartType';
import {AuthenticationService} from '../_services';

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
  headers: HttpHeaders;

  constructor(private http: HttpClient,
              private authService: AuthenticationService) {
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
    this.headers = this.authService.getHeaders();
  }

  predict(): Observable<any> {
    const buildId = '0023499e-0bd1-44bb-b3d5-15da81f0ef12';
    const headers = this.headers;
    return this.http.get(`${this.predictUrl}`, {headers});
  }

  savingThisMonth(): Observable<any> {
    const headers = this.headers;
    return this.http.get(`${this.savingUrl}`, {headers});
  }

  getBEScore(): Observable<any> {
    const headers = this.headers;
    return this.http.get(`${this.beScoreUrl}`, {headers});
  }

  costStackData(): Observable<any> {
    const headers = this.headers;
    return this.http.get(`${this.costStackUrl}`, {headers});
  }

  costPieData(): Observable<any> {
    const headers = this.headers;
    return this.http.get(`${this.costPieUrl}`, {headers});
  }

  consumptionStackData(): Observable<any> {
    const headers = this.headers;
    return this.http.get(`${this.consumptionUrl}`, {headers});
  }

  consumptionDynamicData(year: YearFilterType,
                         timePeriod: TimePeriodType,
                         datePartType: DatePartType): Observable<any> {
    const headers = this.headers;
    const params = new HttpParams()
      .set('year', year)
      .set('periodType', timePeriod)
      .set('datePartType', datePartType);
    return this.http.get(`${this.consumptionDynamicUrl}`,
      {headers, params}
    );
  }

  normalizedConsumptionWeatherData(): Observable<any> {
    const headers = this.headers;
    return this.http.get(`${this.normalizedConsumptionWeatherUrl}`, {headers});
  }

  normalizedPerCapitaData(): Observable<any> {
    const headers = this.headers;
    return this.http.get(`${this.normalizedPerCapitaUrl}`, {headers});
  }

  normalizedVsEnergyEfficiency(): Observable<any> {
    const headers = this.headers;
    return this.http.get(`${this.normalizedVsEEUrl}`, {headers});
  }

  predictedWeatherVSReal(): Observable<any> {
    const headers = this.headers;
    return this.http.get(`${this.predictedWeatherVSRealUrl}`, {headers});
  }

  carbonPieData(): Observable<any> {
    const headers = this.headers;
    return this.http.get(`${this.carbonPieDataUrl}`, {headers});
  }

  carbonSPLineData(): Observable<any> {
    const headers = this.headers;
    return this.http.get(`${this.carbonSPLineUrl}`, {headers});
  }

  getAllEnergyConsumptionIndexes(): Observable<any> {
    const headers = this.headers;
    return this.http.get(`${this.energyConsumptionIndexUrl}`, {headers});
  }

  getNationalMedian(): Observable<any> {
    const headers = this.headers;
    return this.http.get(`${this.nationalMedianUrl}`, {headers});
  }

  getPropertyTarget(): Observable<any> {
    const headers = this.headers;
    return this.http.get(`${this.propertyTargetUrl}`, {headers});
  }
}
