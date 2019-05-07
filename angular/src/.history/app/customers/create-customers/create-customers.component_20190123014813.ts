
import { Component, ViewChild, ElementRef, OnInit, Output, EventEmitter } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { CustomerService } from '@shared/service-proxies/service-proxies';
import {Observable} from 'rxjs';
import {startWith, map} from 'rxjs/operators';

@Component({
    selector: 'app-create-customers',
    templateUrl: './create-customers.component.html',
    styleUrls: ['./create-customers.component.css']
})
export class CreateCustomersComponent implements OnInit {
    @ViewChild('createCustumerModal') modal: ModalDirective;
    @ViewChild('modalContent') modalContent: ElementRef;

    // tslint:disable-next-line:no-output-rename
    @Output('value') FV = new EventEmitter();

    // slect countries

    // sekect ciuntries

    active = false;
    addForm: FormGroup;

    constructor(
        private formBuilder: FormBuilder,
        private custumerService: CustomerService) {
    }
    ngOnInit() {
        this.addForm = this.formBuilder.group({
            name: [''],
            country: [''],
            company: [''],
            tel: [''],
            email: ['', [Validators.required, Validators.email]],
            billaddress: [''],
            skypeid: [''],
        });
    }


    show(): void {
        this.active = true;
        this.modal.show();
    }
    close(): void {
        this.active = false;
        this.modal.hide();
    }
    onSubmit() {
        this.custumerService.createCustumer(this.addForm.value)
            .subscribe(data => {
                this.FV.emit(data);
                abp.notify.info('Added Successfully');
                this.close();
            });
    }

}

export interface StateGroup {
    letter: string;
    names: string[];
  }
  export const _filter = (opt: string[], value: string): string[] => {
    const filterValue = value.toLowerCase();
    return opt.filter(item => item.toLowerCase().indexOf(filterValue) === 0);
  };
