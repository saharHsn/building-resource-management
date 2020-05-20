import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BuildingButtonComponent } from './building-button.component';

describe('BuildingButtonComponent', () => {
  let component: BuildingButtonComponent;
  let fixture: ComponentFixture<BuildingButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BuildingButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BuildingButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
