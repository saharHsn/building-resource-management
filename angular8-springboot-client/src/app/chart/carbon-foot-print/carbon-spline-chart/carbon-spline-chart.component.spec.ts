import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CarbonSplineChartComponent} from './carbon-spline-chart.component';

describe('CarbonSplineChartComponent', () => {
  let component: CarbonSplineChartComponent;
  let fixture: ComponentFixture<CarbonSplineChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CarbonSplineChartComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CarbonSplineChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
