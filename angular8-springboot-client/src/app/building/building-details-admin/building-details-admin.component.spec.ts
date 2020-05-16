import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {BuildingDetailsAdminComponent} from './building-details-admin.component';

describe('BuildingDetailsAdminComponent', () => {
  let component: BuildingDetailsAdminComponent;
  let fixture: ComponentFixture<BuildingDetailsAdminComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [BuildingDetailsAdminComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BuildingDetailsAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
