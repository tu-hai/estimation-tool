import { Component, Injector, AfterViewInit } from '@angular/core';
import { AppComponentBase } from '@shared/app-component-base';
import { appModuleAnimation } from '@shared/animations/routerTransition';
@Component({
    templateUrl: './home.component.html',
    animations: [appModuleAnimation()]
})
export class HomeComponent extends AppComponentBase implements AfterViewInit {
    totalElement: any;
    a: 17;

    constructor(
        injector: Injector,
    ) {
        super(injector);
    }

    ngAfterViewInit(): void {
        this.getTotalElement();
        $(function () {
            $('.count-to').countTo();
            $('.sales-count-to').countTo({
                formatter: function (value) {
                    return '$' + value.toFixed(2).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, ' ').replace('.', ',');
                }
            });
        });
    }

    getTotalElement() {
    this.homePageService.getTotalElement()
    .subscribe(data => {
        this.totalElement = data.customer;
        console.log('ss', this.totalElement);
    })
    }
}
