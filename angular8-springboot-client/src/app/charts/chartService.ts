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
    const buildId = '0023499e-0bd1-44bb-b3d5-15da81f0ef12';
    return this.http.get(`${this.predictUrl}/${buildId}`);
  }

  savingThisMonth(buildingId: string): Observable<any> {
    return this.http.get(`${this.savingUrl}/${'0023499e-0bd1-44bb-b3d5-15da81f0ef12'}`);
  }

  getBEScore(buildingId: any): Observable<any> {
    return this.http.get(`${this.beScoreUrl}/${'0023499e-0bd1-44bb-b3d5-15da81f0ef12'}`);
  }

  costStackData(buildingId: string): Observable<any> {
    return this.http.get(`${this.costStackUrl}/${'0023499e-0bd1-44bb-b3d5-15da81f0ef12'}`);
  }

  costPieData(buildingId: string): Observable<any> {
    return this.http.get(`${this.costPieUrl}/${'0023499e-0bd1-44bb-b3d5-15da81f0ef12'}`);
  }

  consumptionStackData(buildingId: string): Observable<any> {
    return this.http.get(`${this.consumptionUrl}/${'0023499e-0bd1-44bb-b3d5-15da81f0ef12'}`);
  }

  consumptionDynamicData(buildingId: string,
                         year: YearFilterType,
                         timePeriod: TimePeriodType,
                         datePartType: DatePartType): Observable<any> {
    const params = new HttpParams()
      .set('year', year)
      .set('periodType', timePeriod)
      .set('datePartType', datePartType);
    return this.http.get(`${this.consumptionDynamicUrl}/${'0023499e-0bd1-44bb-b3d5-15da81f0ef12'}`,
      {params}
    );
  }

  normalizedConsumptionWeatherData(buildingId: string): Observable<any> {
    return this.http.get(`${this.normalizedConsumptionWeatherUrl}/${'0023499e-0bd1-44bb-b3d5-15da81f0ef12'}`);
  }

  normalizedPerCapitaData(buildingId: string): Observable<any> {
    return this.http.get(`${this.normalizedPerCapitaUrl}/${'0023499e-0bd1-44bb-b3d5-15da81f0ef12'}`);
  }

  normalizedVsEnergyEfficiency(buildingId: string): Observable<any> {
    return this.http.get(`${this.normalizedVsEEUrl}/${'0023499e-0bd1-44bb-b3d5-15da81f0ef12'}`);
  }

  predictedWeatherVSReal(buildingId: string): Observable<any> {
    return this.http.get(`${this.predictedWeatherVSRealUrl}/${'0023499e-0bd1-44bb-b3d5-15da81f0ef12'}`);
  }

  carbonPieData(buildingId: string): Observable<any> {
    return this.http.get(`${this.carbonPieDataUrl}/${'0023499e-0bd1-44bb-b3d5-15da81f0ef12'}`);
  }

  carbonSPLineData(buildingId: string): Observable<any> {
    return this.http.get(`${this.carbonSPLineUrl}/${'0023499e-0bd1-44bb-b3d5-15da81f0ef12'}`);
  }

  getAllEnergyConsumptionIndexes(buildingId: number): Observable<any> {
    return this.http.get(`${this.energyConsumptionIndexUrl}/${'0023499e-0bd1-44bb-b3d5-15da81f0ef12'}`);
  }

  getNationalMedian(buildingId: number): Observable<any> {
    return this.http.get(`${this.nationalMedianUrl}/${'0023499e-0bd1-44bb-b3d5-15da81f0ef12'}`);
  }

  getPropertyTarget(buildingId: number): Observable<any> {
    return this.http.get(`${this.propertyTargetUrl}/${'0023499e-0bd1-44bb-b3d5-15da81f0ef12'}`);
  }
}
