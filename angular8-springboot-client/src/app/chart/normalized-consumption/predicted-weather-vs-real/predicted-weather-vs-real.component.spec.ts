import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {PredictedWeatherVsRealComponent} from './predicted-weather-vs-real.component';

describe('PredictedWeatherVsRealComponent', () => {
  let component: PredictedWeatherVsRealComponent;
  let fixture: ComponentFixture<PredictedWeatherVsRealComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [PredictedWeatherVsRealComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PredictedWeatherVsRealComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
