import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CarbonFootPrintComponent} from './carbon-foot-print.component';

describe('CarbonFootPrintComponent', () => {
  let component: CarbonFootPrintComponent;
  let fixture: ComponentFixture<CarbonFootPrintComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CarbonFootPrintComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CarbonFootPrintComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
