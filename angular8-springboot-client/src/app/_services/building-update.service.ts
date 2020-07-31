import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class BuildingUpdateService {
  idBuilding: string;

  constructor() {
    this.idBuilding = localStorage.getItem('idBuilding');
  }

  getIdBuilding() {
    // return this.idBuilding;
    return localStorage.getItem('idBuilding');
  }

  setIdBuilding(id: string) {
    localStorage.setItem('idBuilding', id);

  }
}

