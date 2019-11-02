import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ConsumptionWeatherComponent} from './consumption-weather.component';

describe('ConsumptionWeatherComponent', () => {
  let component: ConsumptionWeatherComponent;
  let fixture: ComponentFixture<ConsumptionWeatherComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ConsumptionWeatherComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsumptionWeatherComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
