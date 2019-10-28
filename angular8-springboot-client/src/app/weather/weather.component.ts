import {Component, OnInit} from '@angular/core';
//import { WeatherSettings, TemperatureScale, ForecastMode, WeatherLayout } from 'angular-weather-widget';
//import {CurrentWeather, Forecast} from "angular-weather-widget/services/api/weather.api.service";

@Component({
  //template: '<weather-widget [currentWeather]=currentWeather [forecast]=forecast  [settings]="settings"></weather-widget>'
  selector: 'app-weather',
  templateUrl: './weather.component.html',
  styleUrls: ['./weather.component.css']
})
export class WeatherComponent implements OnInit {
  //currentWeather: CurrentWeather = CURRENT_WATHER_MOCK;
  //forecast: Forecast[] = FORECAST_MOCK;
  /*settings: WeatherSettings = {
    location: {
      cityName: 'Lisbon'
    },
    backgroundColor: '#347c57',
    color: '#ffffff',
    width: '300px',
    height: 'auto',
    showWind: false,
    scale: TemperatureScale.CELCIUS,
    forecastMode: ForecastMode.DETAILED,
    showDetails: false,
    showForecast: true,
    layout: WeatherLayout.WIDE,
    language: 'en'
  };*/
  constructor() {
  }

  ngOnInit() {
  }

}
