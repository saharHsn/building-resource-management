import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HeatmapDailyComponent } from './heatmap-daily.component';

describe('HeatmapDailyComponent', () => {
  let component: HeatmapDailyComponent;
  let fixture: ComponentFixture<HeatmapDailyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HeatmapDailyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeatmapDailyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
