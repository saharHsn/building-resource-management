import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {NormalizedVsEnergyEfficiencyComponent} from './normalized-vs-energy-efficiency.component';

describe('NormalizedVsEnergyEfficiencyComponent', () => {
  let component: NormalizedVsEnergyEfficiencyComponent;
  let fixture: ComponentFixture<NormalizedVsEnergyEfficiencyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [NormalizedVsEnergyEfficiencyComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NormalizedVsEnergyEfficiencyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
