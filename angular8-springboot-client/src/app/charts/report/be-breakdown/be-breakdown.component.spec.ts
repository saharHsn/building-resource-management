import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {BeBreakdownComponent} from './be-breakdown.component';

describe('BEBreakDownComponent', () => {
  let component: BeBreakdownComponent;
  let fixture: ComponentFixture<BeBreakdownComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [BeBreakdownComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BeBreakdownComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
