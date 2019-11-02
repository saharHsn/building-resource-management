import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {NormalizedPerCapitaComponent} from './normalized-per-capita.component';

describe('NormalizedPerCapitaComponent', () => {
  let component: NormalizedPerCapitaComponent;
  let fixture: ComponentFixture<NormalizedPerCapitaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [NormalizedPerCapitaComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NormalizedPerCapitaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
