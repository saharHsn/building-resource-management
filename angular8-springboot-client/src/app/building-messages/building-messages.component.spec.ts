import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {BuildingMessagesComponent} from './building-messages.component';

describe('BuildingMessagesComponent', () => {
  let component: BuildingMessagesComponent;
  let fixture: ComponentFixture<BuildingMessagesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [BuildingMessagesComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BuildingMessagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
