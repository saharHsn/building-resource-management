import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AverageSolarComponent } from './average-solar.component';

describe('AverageSolarComponent', () => {
  let component: AverageSolarComponent;
  let fixture: ComponentFixture<AverageSolarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AverageSolarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AverageSolarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
