import { TestBed } from '@angular/core/testing';

import { TimeServicesService } from './time-services.service';

describe('TimeServicesService', () => {
  let service: TimeServicesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TimeServicesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
