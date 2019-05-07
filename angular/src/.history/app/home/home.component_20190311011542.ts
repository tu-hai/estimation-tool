import { Component, Injector, AfterViewInit } from '@angular/core';
import { AppComponentBase } from '@shared/app-component-base';
import { appModuleAnimation } from '@shared/animations/routerTransition';
@Component({
    templateUrl: './home.component.html',
    animations: [appModuleAnimation()]
})
export class HomeComponent extends AppComponentBase implements AfterViewInit {
    totalElement: any;

    constructor(
        injector: Injector,
    ) {
        super(injector);
    }
    ngAfterViewInit(): void {
        $(function () {
            // Widgets count
            $('.count-to').countTo();

            // Sales count to
            $('.sales-count-to').countTo({
                formatter: function (value, options) {
                    return '$' + value.toFixed(2).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, ' ').replace('.', ',');
                }
            });
        });
    }
}
