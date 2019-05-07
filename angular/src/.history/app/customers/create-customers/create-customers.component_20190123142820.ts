
import { Component, ViewChild, ElementRef, OnInit, Output, EventEmitter } from '@angular/core';
import { AfterViewInit, OnDestroy } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import { FormBuilder, FormGroup, Validators, AbstractControl, FormControl } from '@angular/forms';
import { CustomerService } from '@shared/service-proxies/service-proxies';
import { MatAutocompleteSelectedEvent, MatAutocompleteTrigger } from '@angular/material';
import {Observable} from 'rxjs';
import {startWith, map} from 'rxjs/operators';
import { Subscription } from 'rxjs/Subscription';

@Component({
    selector: 'app-create-customers',
    templateUrl: './create-customers.component.html',
    styleUrls: ['./create-customers.component.css']
})
export class CreateCustomersComponent implements OnInit, AfterViewInit, OnDestroy {
    stateCtrl = new FormControl;
    filteredStates: Observable<string[] | null>;
    @ViewChild(MatAutocompleteTrigger) trigger: MatAutocompleteTrigger;
    states = [
        'Alabama',
        'Alaska',
        'Arizona',
        'Arkansas',
        'California',
      ];
    subscription: Subscription;
    @ViewChild('createCustumerModal') modal: ModalDirective;
    @ViewChild('modalContent') modalContent: ElementRef;
    // tslint:disable-next-line:no-output-rename
    @Output('value') FV = new EventEmitter();


    active = false;
    addForm: FormGroup;

    constructor(
        private formBuilder: FormBuilder,
        private custumerService: CustomerService) {
            this.filteredStates = this.stateCtrl.valueChanges.pipe(
                startWith(null),
                map(name => this.filterStates(name))
              );
    }
    ngAfterViewInit() {
        this._subscribeToClosingActions();
      }
      ngOnDestroy() {
        if (this.subscription && !this.subscription.closed) {
          this.subscription.unsubscribe();
        }
      }
      private _subscribeToClosingActions(): void {
        if (this.subscription && !this.subscription.closed) {
          this.subscription.unsubscribe();
        }
        this.subscription = this.trigger.panelClosingActions
          .subscribe(e => {
            if (!e || !e.source) {
              this.stateCtrl.setValue(null);
            }
          },
          err => this._subscribeToClosingActions(),
          () => this._subscribeToClosingActions());
      }
      handler(event: MatAutocompleteSelectedEvent): void {
        this.stateCtrl.setValue(event.option.value);
      }
      filterStates(val: string): string[] {
        return val ? this.states.filter(s => s.toLowerCase().indexOf(val.toLowerCase()) > -1)
          : this.states;
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
