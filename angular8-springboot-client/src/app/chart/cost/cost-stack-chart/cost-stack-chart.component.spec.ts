import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CostStackChartComponent} from './cost-stack-chart.component';

describe('CostStackChartComponent', () => {
  let component: CostStackChartComponent;
  let fixture: ComponentFixture<CostStackChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CostStackChartComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CostStackChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
