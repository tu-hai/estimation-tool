
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
    stateForm: FormGroup = this.fb.group({
        stateGroup: '',
      });
      stateGroups: StateGroup[] = [{
        letter: 'A',
        names: ['Alabama', 'Alaska', 'Arizona', 'Arkansas']
      }, {
        letter: 'C',
        names: ['California', 'Colorado', 'Connecticut']
      }, {
        letter: 'D',
        names: ['Delaware']
      }, {
        letter: 'F',
        names: ['Florida']
      }, {
        letter: 'G',
        names: ['Georgia']
      }, {
        letter: 'H',
        names: ['Hawaii']
      }, {
        letter: 'I',
        names: ['Idaho', 'Illinois', 'Indiana', 'Iowa']
      }, {
        letter: 'K',
        names: ['Kansas', 'Kentucky']
      }, {
        letter: 'L',
        names: ['Louisiana']
      }, {
        letter: 'M',
        names: ['Maine', 'Maryland', 'Massachusetts', 'Michigan',
          'Minnesota', 'Mississippi', 'Missouri', 'Montana']
      }, {
        letter: 'N',
        names: ['Nebraska', 'Nevada', 'New Hampshire', 'New Jersey',
          'New Mexico', 'New York', 'North Carolina', 'North Dakota']
      }, {
        letter: 'O',
        names: ['Ohio', 'Oklahoma', 'Oregon']
      }, {
        letter: 'P',
        names: ['Pennsylvania']
      }, {
        letter: 'R',
        names: ['Rhode Island']
      }, {
        letter: 'S',
        names: ['South Carolina', 'South Dakota']
      }, {
        letter: 'T',
        names: ['Tennessee', 'Texas']
      }, {
        letter: 'U',
        names: ['Utah']
      }, {
        letter: 'V',
        names: ['Vermont', 'Virginia']
      }, {
        letter: 'W',
        names: ['Washington', 'West Virginia', 'Wisconsin', 'Wyoming']
      }];
      stateGroupOptions: Observable<StateGroup[]>;
    // sekect ciuntries

    active = false;
    addForm: FormGroup;

    constructor(
        private formBuilder: FormBuilder,
        private custumerService: CustomerService,
        private fb: FormBuilder) {
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

        this.stateGroupOptions = this.stateForm.get('stateGroup')!.valueChanges
      .pipe(
        startWith(''),
        map(value => this._filterGroup(value))
      );
    }

    private _filterGroup(value: string): StateGroup[] {
        if (value) {
          return this.stateGroups
            .map(group => ({letter: group.letter, names: _filter(group.names, value)}))
            .filter(group => group.names.length > 0);
        }
        return this.stateGroups;
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
