import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HeatmapHourlyComponent } from './heatmap-hourly.component';

describe('HeatmapHourlyComponent', () => {
  let component: HeatmapHourlyComponent;
  let fixture: ComponentFixture<HeatmapHourlyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HeatmapHourlyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeatmapHourlyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
