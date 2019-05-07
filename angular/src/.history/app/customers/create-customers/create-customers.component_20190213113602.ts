
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
    // tslint:disable-next-line:max-line-length
    options: string[] = ['Afghanistan', 'Albania', 'Algeria', 'Andorra', 'Angola', 'Anguilla', 'Antigua & Barbuda', 'Argentina', 'Armenia', 'Aruba', 'Australia', 'Austria', 'Azerbaijan', 'Bahamas', 'Bahrain', 'Bangladesh', 'Barbados', 'Belarus', 'Belgium', 'Belize', 'Benin', 'Bermuda', 'Bhutan', 'Bolivia', 'Bosnia & Herzegovina', 'Botswana', 'Brazil', 'British Virgin Islands', 'Brunei', 'Bulgaria', 'Burkina Faso', 'Burundi', 'Cambodia', 'Cameroon', 'Canada', 'Cape Verde', 'Cayman Islands', 'Central Arfrican Republic', 'Chad', 'Chile', 'China', 'Colombia', 'Congo', 'Cook Islands', 'Costa Rica', 'Cote D Ivoire', 'Croatia', 'Cuba', 'Curacao', 'Cyprus', 'Czech Republic', 'Denmark', 'Djibouti', 'Dominica', 'Dominican Republic', 'Ecuador', 'Egypt', 'El Salvador', 'Equatorial Guinea', 'Eritrea', 'Estonia', 'Ethiopia', 'Falkland Islands', 'Faroe Islands', 'Fiji', 'Finland', 'France', 'French Polynesia', 'French West Indies', 'Gabon', 'Gambia', 'Georgia', 'Germany', 'Ghana', 'Gibraltar', 'Greece', 'Greenland', 'Grenada', 'Guam', 'Guatemala', 'Guernsey', 'Guinea', 'Guinea Bissau', 'Guyana', 'Haiti', 'Honduras', 'Hong Kong', 'Hungary', 'Iceland', 'India', 'Indonesia', 'Iran', 'Iraq', 'Ireland', 'Isle of Man', 'Israel', 'Italy', 'Jamaica', 'Japan', 'Jersey', 'Jordan', 'Kazakhstan', 'Kenya', 'Kiribati', 'Kosovo', 'Kuwait', 'Kyrgyzstan', 'Laos', 'Latvia', 'Lebanon', 'Lesotho', 'Liberia', 'Libya', 'Liechtenstein', 'Lithuania', 'Luxembourg', 'Macau', 'Macedonia', 'Madagascar', 'Malawi', 'Malaysia', 'Maldives', 'Mali', 'Malta', 'Marshall Islands', 'Mauritania', 'Mauritius', 'Mexico', 'Micronesia', 'Moldova', 'Monaco', 'Mongolia', 'Montenegro', 'Montserrat', 'Morocco', 'Mozambique', 'Myanmar', 'Namibia', 'Nauro', 'Nepal', 'Netherlands', 'Netherlands Antilles', 'New Caledonia', 'New Zealand', 'Nicaragua', 'Niger', 'Nigeria', 'North Korea', 'Norway', 'Oman', 'Pakistan', 'Palau', 'Palestine', 'Panama', 'Papua New Guinea', 'Paraguay', 'Peru', 'Philippines', 'Poland', 'Portugal', 'Puerto Rico', 'Qatar', 'Reunion', 'Romania', 'Russia', 'Rwanda', 'Saint Pierre & Miquelon', 'Samoa', 'San Marino', 'Sao Tome and Principe', 'Saudi Arabia', 'Senegal', 'Serbia', 'Seychelles', 'Sierra Leone', 'Singapore', 'Slovakia', 'Slovenia', 'Solomon Islands', 'Somalia', 'South Africa', 'South Korea', 'South Sudan', 'Spain', 'Sri Lanka', 'St Kitts & Nevis', 'St Lucia', 'St Vincent', 'Sudan', 'Suriname', 'Swaziland', 'Sweden', 'Switzerland', 'Syria', 'Taiwan', 'Tajikistan', 'Tanzania', 'Thailand', 'Timor L\'Este', 'Togo', 'Tonga', 'Trinidad & Tobago', 'Tunisia', 'Turkey', 'Turkmenistan', 'Turks & Caicos', 'Tuvalu', 'Uganda', 'Ukraine', 'United Arab Emirates', 'United Kingdom', 'United States of America', 'Uruguay', 'Uzbekistan', 'Vanuatu', 'Vatican City', 'Venezuela', 'Vietnam', 'Virgin Islands (US)', 'Yemen', 'Zambia', 'Zimbabwe'];
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
        this.filteredOptions = this.myControl.valueChanges
        .pipe(
          startWith(''),
          map(value => this._filter(value))
        );
        this.addForm = this.formBuilder.group({
            name: [''],
            country: [this.myControl],
            company: [''],
            tel: [''],
            email: ['', [Validators.required, Validators.email]],
            billaddress: [''],
            skypeid: [''],
        });

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
