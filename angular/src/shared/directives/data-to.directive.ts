import { Directive, ElementRef, Injectable, AfterViewInit, Input } from '@angular/core';

@Directive({
    selector: '[data-to]'
})

@Injectable()
export class DataToDirective {
    constructor(private _element: ElementRef) {
    }
}
