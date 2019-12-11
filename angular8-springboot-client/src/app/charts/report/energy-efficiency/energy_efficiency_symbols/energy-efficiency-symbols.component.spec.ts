import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {EnergyEfficiencySymbolsComponent} from './energy-efficiency-symbols.component';


describe('EnergyEfficiencyComponent', () => {
  let component: EnergyEfficiencySymbolsComponent;
  let fixture: ComponentFixture<EnergyEfficiencySymbolsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [EnergyEfficiencySymbolsComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EnergyEfficiencySymbolsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
