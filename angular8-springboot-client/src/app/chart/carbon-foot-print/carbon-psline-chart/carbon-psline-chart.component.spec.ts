import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CarbonPslineChartComponent} from './carbon-psline-chart.component';

describe('CarbonPslineChartComponent', () => {
  let component: CarbonPslineChartComponent;
  let fixture: ComponentFixture<CarbonPslineChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CarbonPslineChartComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CarbonPslineChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
