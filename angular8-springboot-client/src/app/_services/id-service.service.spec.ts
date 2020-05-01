import {TestBed} from '@angular/core/testing';

import {BuildingUpdateService} from './building-update.service';

describe('IdServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: BuildingUpdateService = TestBed.get(BuildingUpdateService);
    expect(service).toBeTruthy();
  });
});
