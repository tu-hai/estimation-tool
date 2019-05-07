
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
  editForm:  FormGroup;
  listworkItem: FormArray;
  quoctationById: any;
  get formData(): any { return this.editForm.get('wordItemDTOList'); }

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
  }

  formSubmit() {
    this.editForm = this.formBuilder.group({
      'id': new FormControl,
      'wordItemDTOList': new FormArray([
          new FormGroup({
          'no': new FormControl(''),
          'codingErffort': new FormControl(''),
          'indayte': new FormControl(''),
          'assumptionNote': new FormControl(''),
          'assumptionConten': new FormControl(''),
          'taskName': new FormControl(''),
        })
      ]),
    })
  }

  removeTask(index: number) {
    (<FormArray>this.editForm.get('listworkItem')).removeAt(index);
    (<FormArray>this.editForm.get('assumptionList')).removeAt(index);
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
        console.log('check', data)
        this.quoctationById = Array.of(this.quoctationById);
    });
    this.active = true;
    this.modal.show();
  }

  close(): void {
    this.active = false;
    this.modal.hide();
  }

  onSubmit() {
    this.quotationservice.editQuotation(this.editForm.value)
      .subscribe(data => {
          this.FV.emit();
          abp.notify.info('Edit Successfully');
          this.close();
      });
  }
}
