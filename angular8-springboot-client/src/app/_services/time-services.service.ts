import {Injectable} from '@angular/core';


interface Select {
  value: string;
  viewValue: string;
}
@Injectable({
  providedIn: 'root'
})
export class TimeServicesService {

  months: any;
  years: any;
  year: string;
  month: string;


  constructor() {
    this.year = String(new Date().getFullYear().toString());  /* new Date().getFullYear().toString(); */
    this.month = String(new Date().getMonth());
  }
  getMonths() {
    this.months = [
      { value: '1', viewValue: 'January' },
      { value: '2', viewValue: 'February' },
      { value: '3', viewValue: 'March' },
      { value: '4', viewValue: 'April' },
      { value: '5', viewValue: 'May' },
      { value: '6', viewValue: 'June' },
      { value: '7', viewValue: 'July' },
      { value: '8', viewValue: 'August' },
      { value: '9', viewValue: 'September' },
      { value: '10', viewValue: 'October' },
      { value: '11', viewValue: 'November' },
      { value: '12', viewValue: 'December' },
    ];
    return this.months;
  }
  getYears() {
    this.years = [
      { value: '2019', viewValue: '2019' },
      { value: '2020', viewValue: '2020' },

    ];

    return this.years;
  }

  currentMonth() {
    return this.month;
  }
  currentYear() {
    return this.year;
  }












}
