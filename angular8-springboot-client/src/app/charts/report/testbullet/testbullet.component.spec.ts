import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TestbulletComponent } from './testbullet.component';

describe('TestbulletComponent', () => {
  let component: TestbulletComponent;
  let fixture: ComponentFixture<TestbulletComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TestbulletComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TestbulletComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
