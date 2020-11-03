import { Component, OnInit } from '@angular/core';
import { UserBean } from '../../api/models/user-bean';
import { UserManagementService } from '../../api/services/user-management.service';
import { FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';
import { debounceTime } from 'rxjs/operators'; 

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
 
  users: UserBean[];

  filterGroup:FormGroup ;
 
  constructor(private userService: UserManagementService) {
    this.filterGroup = this.createFormGroup();

  }
 
  ngOnInit() {
     console.log('user list init: ');
    this.filterGroup.get('criteria').valueChanges.pipe( debounceTime(1000) ).subscribe(
      field=> { 
        console.log('criteria changed: ' + field);
        this.userService.findUsers(
          {
          criteria:field,
          start:0,
          howMany:20
          }
          ).subscribe(data => {this.users=data;})
      }
    );


    this.userService.listAllUsers().subscribe(data => {
      this.users = data;
    });
  }

  createFormGroup() {
    return new FormGroup({
      criteria: new FormControl('')
    });
  }

}

