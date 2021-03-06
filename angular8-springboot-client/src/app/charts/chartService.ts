import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {YearFilterType} from './consumption/consumption-average-tariff-cost/filter-form/enum/YearFilterType';
import {TimePeriodType} from './consumption/consumption-average-tariff-cost/filter-form/enum/TimePeriodType';
import {DatePartType} from './consumption/consumption-average-tariff-cost/filter-form/enum/DatePartType';
import {AuthenticationService} from '../_services';
import {BuildingUpdateService} from '../_services/building-update.service';
import {map} from 'rxjs/operators';

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
  private readonly currentMonthSummaryUrl;
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
  private readonly downloadUrl;
  private readonly HistoricalConsumptionUrl;
  private readonly HistoricalCostUrl;
  private readonly HeatMapHourly;
  private readonly HeatMapDaily;
  private readonly solarSaving;
  private readonly averageSolarReq;
  headers: HttpHeaders;

  constructor(private http: HttpClient,
              private authService: AuthenticationService, private buildingUpdateService: BuildingUpdateService ) {

    this.environmentName = environment.environmentName;
    this.environmentUrl = environment.apiUrl;
    this.baseUrl = this.environmentUrl + '/reports';
    this.predictUrl = this.baseUrl + '/prediction';
    this.savingUrl = this.baseUrl + '/saving';
    this.currentMonthSummaryUrl = this.baseUrl + '/currentMonthSummary';
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
    this.downloadUrl = this.baseUrl + '/download';
    this.HistoricalConsumptionUrl = this.baseUrl + '/historicalConsumption';
    this.HistoricalCostUrl = this.baseUrl + '/historicalCost';
    this.HeatMapHourly = this.baseUrl + '/heatMapHourlyCons';
    this.HeatMapDaily = this.baseUrl + '/heatMapDailyCons';
    this.solarSaving = this.baseUrl + '/solarSaving';
    this.averageSolarReq = this.baseUrl + '/averageSolarReq';
    this.headers = this.authService.getHeaders();
  }


  /* month:any,year:any */
  gethistoricalConsumption(month: string, year: string): Observable<any> {
    const buildingId = this.buildingUpdateService.getIdBuilding();
    let headers;
    // @ts-ignore
    const user = this.authService.currentUserValue.id ? this.authService.currentUserValue : this.authService.currentUserValue.content.user;
    if (user && user.token) {
      headers = new HttpHeaders()
        .set('X-Session', user.token)
        .set('Accept', '*/*')
        .set('Content-Type', 'application/json');
    }
    const params = new HttpParams()
      .set('year', year)
      .set('month', month);
    return this.http.get(`${this.HistoricalConsumptionUrl}/${buildingId}`,
      {headers, params}
    );
  }


  getHistoricalCost(month: string, year: string): Observable<any> {
    const buildingId = this.buildingUpdateService.getIdBuilding();
    let headers;
    // @ts-ignore
    const user = this.authService.currentUserValue.id ? this.authService.currentUserValue : this.authService.currentUserValue.content.user;
    if (user && user.token) {
      headers = new HttpHeaders()
        .set('X-Session', user.token)
        .set('Accept', '*/*')
        .set('Content-Type', 'application/json');
    }
    const params = new HttpParams()
      .set('year', year)
      .set('month', month);
    return this.http.get(`${this.HistoricalCostUrl}/${buildingId}`,
      {headers, params}
    );
  }

  getHeatMapHourly(month: string, year: string): Observable<any> {
    const buildingId = this.buildingUpdateService.getIdBuilding();
    let headers;
    // @ts-ignore
    const user = this.authService.currentUserValue.id ? this.authService.currentUserValue : this.authService.currentUserValue.content.user;
    if (user && user.token) {
      headers = new HttpHeaders()
        .set('X-Session', user.token)
        .set('Accept', '*/*')
        .set('Content-Type', 'application/json');
    }
    const params = new HttpParams()
      .set('year', year)
      .set('month', month);
    return this.http.get(`${this.HeatMapHourly}/${buildingId}`,
      {headers, params}
    );
  }
   getHeatMapDaily(year: string): Observable<any> {
    const buildingId = this.buildingUpdateService.getIdBuilding();
    let headers;
    // @ts-ignore
    const user = this.authService.currentUserValue.id ? this.authService.currentUserValue : this.authService.currentUserValue.content.user;
    if (user && user.token) {
      headers = new HttpHeaders()
        .set('X-Session', user.token)
        .set('Accept', '*/*')
        .set('Content-Type', 'application/json');
    }
    const params = new HttpParams()
      .set('year', year);

    return this.http.get(`${this.HeatMapDaily}/${buildingId}`,
      {headers, params}
    ).pipe(map((data: any) => {

      return data.content.dataMatrix;
    }));
  }






  getSolarSaving(year: string): Observable<any> {
    const buildingId = this.buildingUpdateService.getIdBuilding();
    let headers;
    // @ts-ignore
    const user = this.authService.currentUserValue.id ? this.authService.currentUserValue : this.authService.currentUserValue.content.user;
    if (user && user.token) {
      headers = new HttpHeaders()
        .set('X-Session', user.token)
        .set('Accept', '*/*')
        .set('Content-Type', 'application/json');
    }
    const params = new HttpParams()
      .set('year', year);

    return this.http.get(`${this.solarSaving}/${buildingId}`,
      {headers, params}
    ).pipe(map((data: any) => {

      return data.content;
    }));
  }
  getAverageSolarReq(year: string): Observable<any> {
    const buildingId = this.buildingUpdateService.getIdBuilding();
    let headers;
    // @ts-ignore
    const user = this.authService.currentUserValue.id ? this.authService.currentUserValue : this.authService.currentUserValue.content.user;
    if (user && user.token) {
      headers = new HttpHeaders()
        .set('X-Session', user.token)
        .set('Accept', '*/*')
        .set('Content-Type', 'application/json');
    }
    const params = new HttpParams()
      .set('year', year);

    return this.http.get(`${this.averageSolarReq}/${buildingId}`,
      {headers, params}
    ).pipe(map((data: any) => {

      return data.content;
    }));
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
    const objectObservable = this.http.get(`${restUrl}/${idcurrentBuilding}`, {headers});
    return objectObservable;
  }

  savingThisMonth(): Observable<any> {
    return this.callService(`${this.savingUrl}`);
  }

  download(): Observable<any> {
    const idcurrentBuilding = this.buildingUpdateService.getIdBuilding();
    /*.subscribe(response => this.downLoadFile(response, "application/ms-excel"))*/
    // return this.callService(`${this.downloadUrl}`);
    let headers;
    // @ts-ignore
    const user = this.authService.currentUserValue.id ? this.authService.currentUserValue : this.authService.currentUserValue.content.user;
    if (user && user.token) {
      headers = new HttpHeaders()
        .set('X-Session', user.token)
        .set('Accept', '*/*');
    }
    return this.http.get(`${this.downloadUrl}/${idcurrentBuilding}`, {responseType: 'arraybuffer', headers});
  }


  currentMonthSummary(): Observable<any> {
    return this.callService(`${this.currentMonthSummaryUrl}`);
  }


  costStackData(): Observable<any> {
    return this.callService(`${this.costStackUrl}`);
  }

  costPieData(): Observable<any> {
    return this.callService(`${this.costPieUrl}`);
  }

  consumptionStackData(): Observable<any> {
    return this.callService(`${this.consumptionUrl}`);
  }

  consumptionDynamicData(year: YearFilterType,
                         timePeriod: TimePeriodType,
                         datePartType: DatePartType): Observable<any> {
    let headers;
    // @ts-ignore
    const user = this.authService.currentUserValue.id ? this.authService.currentUserValue : this.authService.currentUserValue.content.user;
    if (user && user.token) {
      headers = new HttpHeaders()
        .set('X-Session', user.token)
        .set('Accept', '*/*')
        .set('Content-Type', 'application/json');
    }
    const params = new HttpParams()
      .set('year', year)
      .set('periodType', timePeriod)
      .set('datePartType', datePartType);
    return this.http.get(`${this.consumptionDynamicUrl}`,
      {headers, params}
    );
  }

  normalizedConsumptionWeatherData(): Observable<any> {
    return this.callService(`${this.normalizedConsumptionWeatherUrl}`);
  }

  normalizedPerCapitaData(): Observable<any> {
    return this.callService(`${this.normalizedPerCapitaUrl}`);
  }

  normalizedVsEnergyEfficiency(): Observable<any> {
    return this.callService(`${this.normalizedVsEEUrl}`);
  }

  predictedWeatherVSReal(): Observable<any> {
    return this.callService(`${this.predictedWeatherVSRealUrl}`);
  }

  carbonPieData(): Observable<any> {
    return this.callService(`${this.carbonPieDataUrl}`);
  }

  carbonSPLineData(): Observable<any> {
    return this.callService(`${this.carbonSPLineUrl}`);
  }

  getAllEnergyConsumptionIndexes(): Observable<any> {
    return this.callService(`${this.energyConsumptionIndexUrl}`);
  }

  getNationalMedian(): Observable<any> {
    return this.callService(`${this.nationalMedianUrl}`);
  }

  getPropertyTarget(): Observable<any> {
    return this.callService(`${this.propertyTargetUrl}`);
  }

  predict(): Observable<any> {
    return this.callService(`${this.predictUrl}`);
  }
/*
  getHeatMapHourly(): Observable<any> {
    return this.callService(`${this.predictUrl}`);
  }
  getHeatMapDaily(): Observable<any> {
    return this.callService(`${this.predictUrl}`);
  }
 */
  getBEScore(): Observable<any> {
    return this.callService(`${this.beScoreUrl}`);
  }

}
