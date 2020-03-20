import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ColorEnergyComponent } from './color-energy.component';

describe('ColorEnergyComponent', () => {
  let component: ColorEnergyComponent;
  let fixture: ComponentFixture<ColorEnergyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ColorEnergyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ColorEnergyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
