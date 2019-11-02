import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {NormalizedConsumptionComponent} from './normalized-consumption.component';

describe('NormalizedConsumptionComponent', () => {
  let component: NormalizedConsumptionComponent;
  let fixture: ComponentFixture<NormalizedConsumptionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [NormalizedConsumptionComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NormalizedConsumptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
