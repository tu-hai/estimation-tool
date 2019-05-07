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
  selector: 'app-create-quotation',
  templateUrl: './create-quotation.component.html',
  styleUrls: ['./create-quotation.component.css']
})
export class CreateQuotationComponent implements OnInit {
  // tslint:disable-next-line:no-output-rename
  // @Output('value') FV = new EventEmitter();

  public fieldArray: Array<any> = [];
  public newAttribute: any = {};
  active = false;
  quotation:       Quotation [];
  createQuotation: CreateQuotation [];
  custumer:        Customer  [];
  project:         Project   [];

  createForm:  FormGroup;
  projectID: FormControl;
  customerID: FormControl;
  taskname: FormControl;
  codingEffort: FormControl;
  effortList: FormArray;


  @ViewChild('createQuotationModel') modal: ModalDirective;
  @ViewChild('modalContent') modalContent: ElementRef;


  constructor(
    public quotationservice: QuotationService,
    public custumerService:  CustomerService,
    public projectService:   ProjectService,
    private formBuilder:     FormBuilder,
    ) {}

  ngOnInit() {
    this.getQuotation();
    this.getCustumer();
    this.getProject();
    this.createFormControl();
    this.formSubmit();
  }

  createFormControl() {
    this.projectID     = new FormControl('');
    this.customerID    = new FormControl('');
    this.taskname      = new FormControl('f');
    this.codingEffort  = new FormControl('v');
  }

  formSubmit() {
    this.createForm = new FormGroup({
      projectID: this.projectID,
      customerID: this.customerID,
      listworkItem: this.formBuilder.array([
        this.initListWork()
      ]),
      effortList: new FormArray([])
    })
  }
  initListWork () {
    return this.formBuilder.group({
        taskname: this.taskname, // cho nay chua nhan dc data a
        codingEffort: this.codingEffort
      })
  }
  addFieldValue() {  // add  new input
    const control = this.initListWork();

    this.fieldArray.push(this.initListWork()) // Đây là j z , da cho nay la them moi input a
    // this.newAttribute = null;
    return control;
  }

  deleteFieldValue(index) {
    this.fieldArray.splice(index, 1);
  }

  getQuotation(): void {
    this.quotationservice.getQuotation()
    .subscribe(data => {
      this.quotation = data
    });
  }

  getProject(): void {
    this.projectService.getProjects()
    .subscribe(data => {
      this.project = data
    });
  }

  getCustumer(): void {
    this.custumerService.getCustomer()
    .subscribe(data => {
      this.custumer = data
    });
  }

  onSubmit() {
     this.quotationservice.createQuotation(this.createForm.value)
       .subscribe(data => {
         abp.notify.info('Added Successfully');
         this.close();
   });
   }

  show(): void {
    this.active = true;
    this.modal.show();
  }

  close(): void {
    this.active = false;
    this.modal.hide();
   console.log('helloVN', this.createForm.value)

  }
}
