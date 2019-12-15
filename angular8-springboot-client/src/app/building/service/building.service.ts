import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Building} from '../model/building';
import {environment} from '../../../environments/environment';
import {User} from '../../user/user';
import {AuthenticationService} from '../../_services';

@Injectable({
  providedIn: 'root'
})
export class BuildingService {

  environmentName = '';
  environmentUrl = 'Debug api';
  private readonly baseUrl;
  private headers: HttpHeaders;

  constructor(private http: HttpClient,
              private authService: AuthenticationService) {
    this.environmentName = environment.environmentName;
    this.environmentUrl = environment.apiUrl;
    this.baseUrl = this.environmentUrl + '/buildings';
  }

  getBuilding(id: number): Observable<any> {
    const headers = this.authService.getHeaders();
    return this.http.get(`${this.baseUrl}/${id}`, {headers});
  }


  createOrUpdateBuilding(building: Building, isNew: boolean): Observable<object> {
    if (isNew) {
      return this.createBuilding(building);
    } else {
      this.updateBuilding(building.id, building);
    }
  }

  createBuilding(building: Building): Observable<object> {
    const headers = this.authService.getHeaders();
    const formData: FormData = this.createFormData(building, null, null);
    const req = new HttpRequest('POST', this.baseUrl, formData, {
      headers,
      reportProgress: true,
      responseType: 'text'
    });
    return this.http.request(req);
  }

  createFormData(object: object, form?: FormData, namespace?: string): FormData {
    const formData = form || new FormData();
    for (const property in object) {
      if (!object.hasOwnProperty(property) || !object[property]) {
        continue;
      }
      const formKey = namespace ? `${namespace}.${property}` : property;
      if (object[property] instanceof Date) {
        formData.append(formKey, object[property].toISOString());
      } else if (typeof object[property] === 'object' && !(object[property] instanceof File)) {
        this.createFormData(object[property], formData, formKey);
      } else {
        formData.append(formKey, object[property]);
      }
    }
    return formData;
  }

  updateBuilding(id: string, value: any): Observable<object> {
    const headers = this.authService.getHeaders();
    return this.http.put(`${this.baseUrl}/${id}`, value, {headers});
  }

  deleteBuilding(id: number): Observable<any> {
    const headers = this.authService.getHeaders();
    return this.http.delete(`${this.baseUrl}/${id}`, {responseType: 'text', headers});
  }

  getBuildingsList(): Observable<any> {
    const headers = this.authService.getHeaders();
    return this.http.get(`${this.baseUrl}`, {headers});
  }

  getBuildingByOwner(user: User): Observable<any> {
    const headers = this.authService.getHeaders();
    const params = new HttpParams()
      .set('userId', user.id);
    return this.http.get(`${this.baseUrl + '/findByOwner'}`, {headers, params});
  }

}
