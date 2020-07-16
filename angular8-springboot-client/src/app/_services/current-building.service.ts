import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CurrentBuildingService {
  buildingId: string;

  constructor() {
    this.buildingId = localStorage.getItem('buildingId');
  }

  getBuildingId() {
    return localStorage.getItem('buildingId');
  }

  setBuildingId(id: string) {
    localStorage.setItem('buildingId', id);
  }
}
