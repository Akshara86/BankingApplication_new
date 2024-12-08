import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoanSectionComponent } from './loan-section.component';

describe('LoanSectionComponent', () => {
  let component: LoanSectionComponent;
  let fixture: ComponentFixture<LoanSectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoanSectionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LoanSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
