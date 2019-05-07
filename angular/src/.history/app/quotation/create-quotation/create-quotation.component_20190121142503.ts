import { Component, OnInit, ViewChild, ElementRef, EventEmitter} from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import { appModuleAnimation } from '@shared/animations/routerTransition';
import { QuotationService, CustomerService, ProjectService } from '@shared/service-proxies/service-proxies';
import { Validators, FormBuilder, FormGroup, FormControl, FormArray, NgForm } from '@angular/forms';
import { Quotation } from './../../../models/quotation.model';
import {Customer} from 'models/customer.model';
import { from } from 'rxjs';
import {Project} from 'models/project'

@Component({
  selector: 'app-create-quotation',
  templateUrl: './create-quotation.component.html',
  styleUrls: ['./create-quotation.component.css']
})
export class CreateQuotationComponent implements OnInit {
  public fieldArray: Array<any> = [];
  public newAttribute: any = {};
  active = false;
  quotation: Quotation [];
  custumer:  Customer  [];
  project:   Project   [];

  @ViewChild('createQuotationModel') modal: ModalDirective;
  @ViewChild('modalContent') modalContent: ElementRef;

  addForm: FormGroup;

  constructor(
    public quotationservice: QuotationService,
    public custumerService: CustomerService,
    public projectService: ProjectService
    ) {}

  ngOnInit() {
    this.getQuotation();
    this.getCustumer();
    this.getProject();
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

  addFieldValue() {
    this.fieldArray.push(this.newAttribute)
    this.newAttribute = {};
  }

  deleteFieldValue(index) {
    this.fieldArray.splice(index, 1);
  }

  show(): void {
    this.active = true;
    this.modal.show();
  }

  close(): void {
    this.active = false;
    this.modal.hide();
    console.log('nono', this.custumer);
    console.log('nono2', this.project)
  }

  save(form: NgForm): void {
  }
}
