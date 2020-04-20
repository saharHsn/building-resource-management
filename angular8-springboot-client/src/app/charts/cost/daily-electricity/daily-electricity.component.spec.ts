import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DailyElectricityComponent } from './daily-electricity.component';

describe('DailyElectricityComponent', () => {
  let component: DailyElectricityComponent;
  let fixture: ComponentFixture<DailyElectricityComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DailyElectricityComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DailyElectricityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
