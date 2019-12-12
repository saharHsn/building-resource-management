import {Injectable} from '@angular/core';
import {HttpClient, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Building} from '../model/building';
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class BuildingService {

  // private baseUrl = 'http://builtrixmetrics-env.qwzndp9hya.us-east-2.elasticbeanstalk.com/builtrix/v1/buildings';
  environmentName = '';
  environmentUrl = 'Debug api';
  private readonly baseUrl;

  constructor(private http: HttpClient) {
    this.environmentName = environment.environmentName;
    this.environmentUrl = environment.apiUrl;
    this.baseUrl = this.environmentUrl + '/buildings';
  }

  getBuilding(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  createBuilding(building: Building): Observable<object> {
    const formData: FormData = this.createFormData(building, null, null);
    const req = new HttpRequest('POST', this.baseUrl, formData, {
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

  updateBuilding(id: number, value: any): Observable<object> {
    return this.http.put(`${this.baseUrl}/${id}`, value);
  }

  deleteBuilding(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, {responseType: 'text'});
  }

  getBuildingsList(): Observable<any> {
    return this.http.get(`${this.baseUrl}`);
  }
}
