import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ConsumptionDynamicBarChartComponent} from './consumption-dynamic-bar-chart.component';

describe('ConsumptionAverageTariffCostComponent', () => {
  let component: ConsumptionDynamicBarChartComponent;
  let fixture: ComponentFixture<ConsumptionDynamicBarChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ConsumptionDynamicBarChartComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsumptionDynamicBarChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
