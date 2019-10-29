import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CarbonPieChartComponent} from './carbon-pie-chart.component';

describe('CarbonPieChartComponent', () => {
  let component: CarbonPieChartComponent;
  let fixture: ComponentFixture<CarbonPieChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CarbonPieChartComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CarbonPieChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
