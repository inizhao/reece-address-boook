import { NgModule } from '@angular/core';

import { ReeceaddressbookSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [ReeceaddressbookSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [ReeceaddressbookSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class ReeceaddressbookSharedCommonModule {}
