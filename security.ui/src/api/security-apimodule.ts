/* tslint:disable */
import { NgModule, ModuleWithProviders } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { SecurityAPIConfiguration, SecurityAPIConfigurationInterface } from './security-apiconfiguration';

import { UserManagementService } from './services/user-management.service';

/**
 * Provider for all SecurityAPI services, plus SecurityAPIConfiguration
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
    SecurityAPIConfiguration,
    UserManagementService
  ],
})
export class SecurityAPIModule {
  static forRoot(customParams: SecurityAPIConfigurationInterface): ModuleWithProviders<SecurityAPIModule> {
    return {
      ngModule: SecurityAPIModule,
      providers: [
        {
          provide: SecurityAPIConfiguration,
          useValue: {rootUrl: customParams.rootUrl}
        }
      ]
    }
  }
}
