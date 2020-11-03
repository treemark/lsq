import { BrowserModule } from '@angular/platform-browser';
import { NgModule,APP_INITIALIZER } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule } from '@angular/forms';
import { HttpClientModule , HttpClient} from '@angular/common/http';
import { AppComponent } from './app.component';
import { ReactiveFormsModule } from '@angular/forms';
import { UserListComponent } from './user-list/user-list.component';
import { UserFormComponent } from './user-form/user-form.component';
import { UserManagementService } from '../api/services/user-management.service';

import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {AppInterceptor} from './app.interceptor';


@NgModule({ 
  declarations: [
    AppComponent,
    UserListComponent,
    UserFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
  	{
      provide: HTTP_INTERCEPTORS,
      useClass: AppInterceptor,
      multi: true,
    },

  	UserManagementService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }