import { Component, Injector, AfterViewInit } from '@angular/core';
import { AppComponentBase } from '@shared/app-component-base';
import { appModuleAnimation } from '@shared/animations/routerTransition';
@Component({
    templateUrl: './home.component.html',
    animations: [appModuleAnimation()]
})
export class HomeComponent extends AppComponentBase implements AfterViewInit {
    // totalElement: any;
    // a: 17;

    constructor(
        injector: Injector,
    ) {
        super(injector);
    }

    ngAfterViewInit(): void {
    }
}
