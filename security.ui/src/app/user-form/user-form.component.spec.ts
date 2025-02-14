import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserForm2Component } from './user-form.component';

describe('UserFormComponent', () => {
  let component: UserForm2Component;
  let fixture: ComponentFixture<UserForm2Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserForm2Component ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserForm2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
