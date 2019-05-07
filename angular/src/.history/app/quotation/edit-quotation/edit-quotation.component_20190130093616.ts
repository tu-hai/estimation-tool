
import { Component, OnInit, ViewChild, ElementRef, EventEmitter, Output} from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import { appModuleAnimation } from '@shared/animations/routerTransition';
import { QuotationService, CustomerService, ProjectService } from '@shared/service-proxies/service-proxies';
import { Validators, FormBuilder, FormGroup, FormControl, FormArray, NgForm } from '@angular/forms';
import { Quotation, CreateQuotation } from './../../../models/quotation.model';
import {Customer} from 'models/customer.model';

import { from } from 'rxjs';
import {Project} from 'models/project'


@Component({
  selector: 'app-edit-quotation',
  templateUrl: './edit-quotation.component.html',
  styleUrls: ['./edit-quotation.component.css']
})
export class EditQuotationComponent implements OnInit {

  public fieldArray: Array<any> = [];
  public newAttribute: any = {};
  active = false;
  quotation:       Quotation [];
  createQuotation: CreateQuotation [];
  custumer:        Customer  [];
  project:         Project   [];
  createForm:  FormGroup;
  listworkItem: FormArray;
  indexSelected = 0;
  quoctationById: any;
  get formData(): any { return this.createForm.get('listworkItem'); }

  @ViewChild('editQuotationModel') modal: ModalDirective;
  @ViewChild('modalContent') modalContent: ElementRef;

  // tslint:disable-next-line:no-output-rename
  @Output('value') FV = new EventEmitter();


  constructor(
    public quotationservice: QuotationService,
    private formBuilder:     FormBuilder,
    ) {}

  ngOnInit() {
    this.getQuotation();
    this.formSubmit();
    this.formSubmit();
  }

  formSubmit() {
    this.createForm = this.formBuilder.group({
      projectID: [''],
      customerID: [''],
      'listworkItem': new FormArray([
        new FormGroup({
          'taskname': new FormControl(''),
          'codingEffort': new FormControl('')
        })
      ]),
      'effortList': new FormArray([]),
      'assumptionList': new FormArray([
        new FormGroup({
          'note': new FormControl(''),
          'content': new FormControl('')
        })
      ])
    })
  }

  addTask() {
    (<FormArray>this.createForm.get('listworkItem')).push(
      new FormGroup({
        'taskname': new FormControl(''),
        'codingEffort': new FormControl('')
        // 'assumptions': new FormControl('')
      })
    ),
    (<FormArray>this.createForm.get('assumptionList')).push(
      new FormGroup({
        'note': new FormControl(''),
        'content': new FormControl('')
      })
    )
  }
  removeTask(index: number) {
    (<FormArray>this.createForm.get('listworkItem')).removeAt(index);
    (<FormArray>this.createForm.get('assumptionList')).removeAt(index);
  }

  getQuotation(): void {
    this.quotationservice.getQuotation()
    .subscribe(data => {
      this.quotation = data
    });
  }

  show(id: number): void {
    this.quotationservice.getQuoctationById(id).subscribe(data => {
      this.quoctationById = data;
      this.quoctationById = Array.of(this.quoctationById);
    });
    this.active = true;
    this.modal.show();
    console.log('sss nhu vai bui', this.quoctationById)
  }

  close(): void {
    this.active = false;
    this.modal.hide();
  }

  onSubmit() {
    this.quotationservice.createQuotation(this.createForm.value)
      .subscribe(data => {
        this.FV.emit();
        abp.notify.info('Added Successfully');
        this.close();
  });
}

}
