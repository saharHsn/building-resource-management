import {TestBed} from '@angular/core/testing';

import {CurrentBuildingService} from './current-building.service';

describe('IdServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CurrentBuildingService = TestBed.get(CurrentBuildingService);
    expect(service).toBeTruthy();
  });
});
