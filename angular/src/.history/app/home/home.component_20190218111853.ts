import { Component, Injector, AfterViewInit } from '@angular/core';
import { AppComponentBase } from '@shared/app-component-base';
import { appModuleAnimation } from '@shared/animations/routerTransition';
import { HomePage } from '@shared/service-proxies/service-proxies';
@Component({
    templateUrl: './home.component.html',
    animations: [appModuleAnimation()]
})
export class HomeComponent extends AppComponentBase implements AfterViewInit {
    totalElement: any;

    constructor(
        injector: Injector,
        private homePageService: HomePage
    ) {
        super(injector);
    }

    ngAfterViewInit(): void {
        this.getTotalElement();
        $(function () {
            $('.count-to').countTo();
            $('.sales-count-to').countTo({
                formatter: function (value, options) {
                    return '$' + value.toFixed(2).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, ' ').replace('.', ',');
                }
            });
        });
    }

    getTotalElement() {
    this.homePageService.getTotalElement()
    .subscribe(data => {
        this.totalElement = data;
    })
    }
}
