/* tslint:disable */
import { Injectable } from '@angular/core';

/**
 * Global configuration for SecurityAPI services
 */
@Injectable({
  providedIn: 'root',
})
export class SecurityAPIConfiguration {
  rootUrl: string = '/api';
}

export interface SecurityAPIConfigurationInterface {
  rootUrl?: string;
}
