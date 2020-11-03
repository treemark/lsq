/* tslint:disable */
import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse, HttpHeaders } from '@angular/common/http';
import { BaseService as __BaseService } from '../base-service';
import { SecurityAPIConfiguration as __Configuration } from '../security-apiconfiguration';
import { StrictHttpResponse as __StrictHttpResponse } from '../strict-http-response';
import { Observable as __Observable } from 'rxjs';
import { map as __map, filter as __filter } from 'rxjs/operators';

import { UserBean } from '../models/user-bean';
@Injectable({
  providedIn: 'root',
})
class UserManagementService extends __BaseService {
  static readonly findUsersPath = '/usermgmt/find';
  static readonly findByEmailPath = '/usermgmt/findByEmail';
  static readonly isUniqueEmailPath = '/usermgmt/isUniqueEmail';
  static readonly updateUserPath = '/usermgmt/user';
  static readonly loadUserPath = '/usermgmt/user/{id}';
  static readonly listAllUsersPath = '/usermgmt/users';

  constructor(
    config: __Configuration,
    http: HttpClient
  ) {
    super(config, http);
  }

  /**
   * @param params The `UserManagementService.FindUsersParams` containing the following parameters:
   *
   * - `start`:
   *
   * - `howMany`:
   *
   * - `criteria`:
   *
   * @return successful operation
   */
  findUsersResponse(params: UserManagementService.FindUsersParams): __Observable<__StrictHttpResponse<Array<UserBean>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    if (params.start != null) __params = __params.set('start', params.start.toString());
    if (params.howMany != null) __params = __params.set('howMany', params.howMany.toString());
    if (params.criteria != null) __params = __params.set('criteria', params.criteria.toString());
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/usermgmt/find`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<UserBean>>;
      })
    );
  }
  /**
   * @param params The `UserManagementService.FindUsersParams` containing the following parameters:
   *
   * - `start`:
   *
   * - `howMany`:
   *
   * - `criteria`:
   *
   * @return successful operation
   */
  findUsers(params: UserManagementService.FindUsersParams): __Observable<Array<UserBean>> {
    return this.findUsersResponse(params).pipe(
      __map(_r => _r.body as Array<UserBean>)
    );
  }

  /**
   * @param email undefined
   * @return successful operation
   */
  findByEmailResponse(email: string): __Observable<__StrictHttpResponse<UserBean>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    if (email != null) __params = __params.set('email', email.toString());
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/usermgmt/findByEmail`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<UserBean>;
      })
    );
  }
  /**
   * @param email undefined
   * @return successful operation
   */
  findByEmail(email: string): __Observable<UserBean> {
    return this.findByEmailResponse(email).pipe(
      __map(_r => _r.body as UserBean)
    );
  }

  /**
   * @param params The `UserManagementService.IsUniqueEmailParams` containing the following parameters:
   *
   * - `excludeId`:
   *
   * - `email`:
   *
   * @return successful operation
   */
  isUniqueEmailResponse(params: UserManagementService.IsUniqueEmailParams): __Observable<__StrictHttpResponse<boolean>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    if (params.excludeId != null) __params = __params.set('excludeId', params.excludeId.toString());
    if (params.email != null) __params = __params.set('email', params.email.toString());
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/usermgmt/isUniqueEmail`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'text'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return (_r as HttpResponse<any>).clone({ body: (_r as HttpResponse<any>).body === 'true' }) as __StrictHttpResponse<boolean>
      })
    );
  }
  /**
   * @param params The `UserManagementService.IsUniqueEmailParams` containing the following parameters:
   *
   * - `excludeId`:
   *
   * - `email`:
   *
   * @return successful operation
   */
  isUniqueEmail(params: UserManagementService.IsUniqueEmailParams): __Observable<boolean> {
    return this.isUniqueEmailResponse(params).pipe(
      __map(_r => _r.body as boolean)
    );
  }

  /**
   * @param body undefined
   */
  updateUserResponse(body?: UserBean): __Observable<__StrictHttpResponse<null>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    __body = body;
    let req = new HttpRequest<any>(
      'POST',
      this.rootUrl + `/usermgmt/user`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<null>;
      })
    );
  }
  /**
   * @param body undefined
   */
  updateUser(body?: UserBean): __Observable<null> {
    return this.updateUserResponse(body).pipe(
      __map(_r => _r.body as null)
    );
  }

  /**
   * @param id undefined
   * @return successful operation
   */
  loadUserResponse(id: number): __Observable<__StrictHttpResponse<UserBean>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;

    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/usermgmt/user/${encodeURIComponent(id)}`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<UserBean>;
      })
    );
  }
  /**
   * @param id undefined
   * @return successful operation
   */
  loadUser(id: number): __Observable<UserBean> {
    return this.loadUserResponse(id).pipe(
      __map(_r => _r.body as UserBean)
    );
  }

  /**
   * @return successful operation
   */
  listAllUsersResponse(): __Observable<__StrictHttpResponse<Array<UserBean>>> {
    let __params = this.newParams();
    let __headers = new HttpHeaders();
    let __body: any = null;
    let req = new HttpRequest<any>(
      'GET',
      this.rootUrl + `/usermgmt/users`,
      __body,
      {
        headers: __headers,
        params: __params,
        responseType: 'json'
      });

    return this.http.request<any>(req).pipe(
      __filter(_r => _r instanceof HttpResponse),
      __map((_r) => {
        return _r as __StrictHttpResponse<Array<UserBean>>;
      })
    );
  }
  /**
   * @return successful operation
   */
  listAllUsers(): __Observable<Array<UserBean>> {
    return this.listAllUsersResponse().pipe(
      __map(_r => _r.body as Array<UserBean>)
    );
  }
}

module UserManagementService {

  /**
   * Parameters for findUsers
   */
  export interface FindUsersParams {
    start: number;
    howMany: number;
    criteria: string;
  }

  /**
   * Parameters for isUniqueEmail
   */
  export interface IsUniqueEmailParams {
    excludeId: number;
    email: string;
  }
}

export { UserManagementService }
