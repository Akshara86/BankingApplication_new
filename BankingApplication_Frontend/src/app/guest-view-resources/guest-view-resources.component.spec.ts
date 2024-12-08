import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GuestViewResourcesComponent } from './guest-view-resources.component';

describe('GuestViewResourcesComponent', () => {
  let component: GuestViewResourcesComponent;
  let fixture: ComponentFixture<GuestViewResourcesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GuestViewResourcesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GuestViewResourcesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
