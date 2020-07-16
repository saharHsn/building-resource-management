import { TestBed } from '@angular/core/testing';

import { HeatmapDailyService } from './heatmap-daily.service';

describe('HeatmapDailyService', () => {
  let service: HeatmapDailyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HeatmapDailyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
