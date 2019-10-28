import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {BeScoreComponent} from './be-score.component';

describe('BeScoreComponent', () => {
  let component: BeScoreComponent;
  let fixture: ComponentFixture<BeScoreComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [BeScoreComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BeScoreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
