import { TestBed } from '@angular/core/testing';

import { IdServiceService } from './id-service.service';

describe('IdServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: IdServiceService = TestBed.get(IdServiceService);
    expect(service).toBeTruthy();
  });
});
