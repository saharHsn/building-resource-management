import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ElectricityConsumptionOverTimeComponent} from './electricity-consumption-over-time.component';

describe('ElectricityConsumptionOverTimeComponent', () => {
  let component: ElectricityConsumptionOverTimeComponent;
  let fixture: ComponentFixture<ElectricityConsumptionOverTimeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ElectricityConsumptionOverTimeComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ElectricityConsumptionOverTimeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
