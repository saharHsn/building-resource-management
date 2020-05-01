import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class IdServiceService {


  idBuilding:string;

  constructor() {
    this.idBuilding=localStorage.getItem('idBuilding');
   }

   getIdbuilding(){
     return this.idBuilding;
   }

   setIdbuilding(id:string){
    localStorage.setItem('idBuilding', id);
   }

   deleteIdbuilding(id:string){
    localStorage.clear();
   }


  }
