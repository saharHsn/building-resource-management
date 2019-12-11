import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ReportBeScoreComponent} from './report-be-score.component';

describe('BeSocreComponent', () => {
  let component: ReportBeScoreComponent;
  let fixture: ComponentFixture<ReportBeScoreComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ReportBeScoreComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportBeScoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
