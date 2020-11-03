/* tslint:disable */
import { NgModule, ModuleWithProviders } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { RentalTxApiConfiguration, RentalTxApiConfigurationInterface } from './rental-tx-api-configuration';

import { UserManagementService } from './services/user-management.service';

/**
 * Provider for all RentalTxApi services, plus RentalTxApiConfiguration
 */
@NgModule({
  imports: [
    HttpClientModule
  ],
  exports: [
    HttpClientModule
  ],
  declarations: [],
  providers: [
    RentalTxApiConfiguration,
    UserManagementService
  ],
})
export class RentalTxApiModule {
  static forRoot(customParams: RentalTxApiConfigurationInterface): ModuleWithProviders<RentalTxApiModule> {
    return {
      ngModule: RentalTxApiModule,
      providers: [
        {
          provide: RentalTxApiConfiguration,
          useValue: {rootUrl: customParams.rootUrl}
        }
      ]
    }
  }
}
