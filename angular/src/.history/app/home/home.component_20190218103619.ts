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
    hai = 29;

    constructor(
        injector: Injector,
        private homePageService: HomePage
    ) {
        super(injector);
    }

    ngAfterViewInit(): void {
        this.getTotalElement();
        $(function () {
            // Widgets count
            $('.count-to').countTo();

            // Sales count to
            $('.sales-count-to').countTo({
                formatter: function (value, options) {
                    return '$' + value.toFixed(2).replace(/(\d)(?=(\d\d\d)+(?!\d))/g, ' ').replace('.', ',');
                }
            });


            // initSparkline();
        });

        // function initSparkline() {
        //     $(".sparkline").each(function () {
        //         var $this = $(this);
        //         $this.sparkline('html', $this.data());
        //     });
        // }

        // var data = [], totalPoints = 110;
        // function getRandomData() {
        //     if (data.length > 0) data = data.slice(1);
        //     while (data.length < totalPoints) {
        //         var prev = data.length > 0 ? data[data.length - 1] : 50, y = prev + Math.random() * 10 - 5;
        //         if (y < 0) { y = 0; } else if (y > 100) { y = 100; }

        //         data.push(y);
        //     }
        //     var res = [];
        //     for (var i = 0; i < data.length; ++i) {
        //         res.push([i, data[i]]);
        //     }
        //     return res;
        // }
    }

    getTotalElement() {
    this.homePageService.getTotalElement()
    .subscribe(data => {
        this.totalElement = data;
    })
    }
}
