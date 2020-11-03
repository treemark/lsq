import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class AppInterceptor implements HttpInterceptor, OnInit {

 	 baseUrl :string='';

    constructor(private http: HttpClient) {
      // var  result  =http.get("/baseUrl");
      // result.subscribe( (url : string) => {
      //   this.baseUrl= url;
      //   console.log("BASE: " + this.baseUrl);
      // })
    }
	//
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    	  console.log('Interceptor hit:' + this.baseUrl + ' ' + req.url);
        //const baseUrl = document.getElementsByTagName('base')[0].href;
        //const apiReq = req.clone({ url: `${baseUrl}${req.url}` });
        return next.handle(req);
    }
    
   ngOnInit() {
   	console.log('Interceptor init:');
    // subscribe when OnInit fires
    //this.route.params.subscribe(params => {
      // now we can do something!
    //});
  }
}