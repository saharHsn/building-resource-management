import {Injectable} from '@angular/core';
import {HttpClient, HttpRequest, HttpEvent} from '@angular/common/http';
import { Observable } from "rxjs";
import {BillType} from "../enums/BillType";

@Injectable({ providedIn: 'root' })
export class BuildingFileService {

  private baseUrl =  'http://localhost:8080/builtrix/v1/buildings/files';

  constructor(private http: HttpClient) {}

  uploadFile(file: File, buildingId: string,billType:BillType): Observable<HttpEvent<{}>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('buildingId', buildingId);
    formData.append('billType', billType);
    const req = new HttpRequest('POST', this.baseUrl, formData, {
      reportProgress: true,
      responseType: 'text'
    });
    return this.http.request(req);
  }

}
