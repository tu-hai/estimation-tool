
import { Component, ViewChild, ElementRef, OnInit, Output, EventEmitter } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import { FormBuilder, FormGroup, Validators, AbstractControl, FormControl } from '@angular/forms';
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

    myControl = new FormControl();
    options: string[] = ['One', 'Two', 'Three'];
    filteredOptions: Observable<string[]>;
    // tslint:disable-next-line:no-output-rename
    @Output('value') FV = new EventEmitter();


    active = false;
    addForm: FormGroup;

    constructor(
        private fb: FormBuilder,
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
        // countries filler
        this.filteredOptions = this.myControl.valueChanges
        .pipe(
          startWith(''),
          map(value => this._filter(value))
        );
    }
    private _filter(value: string): string[] {
        const filterValue = value.toLowerCase();
        return this.options.filter(option => option.toLowerCase().includes(filterValue));
    }

    show(): void {
        this.active = true;
        this.modal.show();
    }
    close(): void {
        this.active = false;
        this.modal.hide();
        console.log('', this.addForm.value);
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
