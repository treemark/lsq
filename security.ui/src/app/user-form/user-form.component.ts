import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserManagementService } from '../../api/services/user-management.service';
import { UserBean } from '../../api/models/user-bean';
import { FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { AbstractControl, AsyncValidatorFn, ValidationErrors } from '@angular/forms';
import { Observable } from 'rxjs/Observable';
import { filter, map, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent {


  userForm: FormGroup;
  id: number;
  user: UserBean;

  constructor(private route: ActivatedRoute, private router: Router, private userService: UserManagementService, private fb: FormBuilder) {
    this.id = +this.route.snapshot.paramMap.get('id');
    console.log('Form created ' + this.id);
    this.userForm = this.createFormGroup();


    if (this.id > 0)
      this.userService.loadUser(this.id)
        .subscribe(user => { this.init(user); });
    else {
      this.user = {
        active: true,
        createDate: "",
        email: "",
        id: 0,
        name: "",
        version: 0
      }
    }
  }

  saveUser(): void {

  }

  ngOnInit() {
  }


  onSubmit() {
    const result: UserBean = Object.assign(this.user, this.userForm.value);
    this.userService.updateUser(result).subscribe(result => this.gotoUserList());
  }

  init(user: UserBean): void {
    this.userForm.get('name').setValue(user.name);
    this.userForm.get('email').setValue(user.email);
    this.user = user;
  }

  createFormGroup() {
    return new FormGroup({
      name: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email], this.emailValidator(this.id))
    });
  }


  public emailValidator(excludeUserId: number): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      return this.userService.findByEmail(control.value).pipe(map((u: UserBean) => {
        let valid: boolean = (u == null || u.id == excludeUserId);
        return (valid) ? null : { emailNotUnique: true };
      }));
    }
  }

  gotoUserList() {
    this.router.navigate(['/users']);
  }

  get name() { return this.userForm.get('name'); }

  get email() { return this.userForm.get('email'); }
}