/* tslint:disable */
import { Injectable } from '@angular/core';

/**
 * Global configuration for RentalTxApi services
 */
@Injectable({
  providedIn: 'root',
})
export class RentalTxApiConfiguration {
  rootUrl: string = '/api';
}

export interface RentalTxApiConfigurationInterface {
  rootUrl?: string;
}
