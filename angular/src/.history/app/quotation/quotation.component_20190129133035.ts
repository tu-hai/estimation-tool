import {OnInit } from '@angular/core';
import {CreateQuotationComponent } from './create-quotation/create-quotation.component'
import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { appModuleAnimation } from '@shared/animations/routerTransition';
import { Quotation } from './../../models/quotation.model';
import { QuotationService } from '@shared/service-proxies/service-proxies';
import { Validators, FormBuilder, FormGroup, FormControl, FormArray } from '@angular/forms';
import { Project } from '../../models/project';
import { ProjectService } from '../../shared/service-proxies/service-proxies';
import { Customer} from './../../models/customer.model';
import { CustomerService } from '../../shared/service-proxies/service-proxies';
import {DetailQuotationComponent} from './detail-quotation/detail-quotation.component'
import { Pipe, PipeTransform } from '@angular/core';


@Component({
  selector: 'app-quotation',
  templateUrl: './quotation.component.html',
  styleUrls: ['./quotation.component.css'],
  animations: [appModuleAnimation()]
})


export class QuotationComponent  implements AfterViewInit, PipeTransform  {
  @ViewChild('createQuotationModel') createQuotationModel: CreateQuotationComponent;
  @ViewChild('detailQuotationModel') detailQuotationModel: DetailQuotationComponent;
  @Pipe({
    name: 'f'
  })
  quotations: Quotation[];
  projectList: Project[];
  custumerList: Customer[];
  public searchText: string;
  public customerData: any;

  optionSelected: number;
  projectSelectedToViewDetail: number;
  form: FormGroup;

  constructor( private quotationservice: QuotationService, private formBuilder: FormBuilder,
    private projectservice: ProjectService, private custumerService: CustomerService) {
        this.form = new FormGroup({
         project  : new FormControl(null)
    })
  };

  ngAfterViewInit() {
      this.getProject();
      this.getCustumer();
      this.getQuotation();
      this.form = this.formBuilder.group({
        name: [null, Validators.required],
        hours: [null, [Validators.required]],
      });

      this.customerData = [
        {'name': 'Anil kumar', 'Age': 34, 'blog' : 'https://code-view.com'},
        {'name': 'Sunil Kumar Singh', 'Age': 28, 'blog' : 'https://code-sample.xyz'},
        {'name': 'Sushil Singh', 'Age': 24, 'blog' : 'https://code-sample.com'},
        {'name': 'Aradhya Singh', 'Age': 5, 'blog' : 'https://code-sample.com'},
        {'name': 'Reena Singh', 'Age': 28, 'blog' : 'https://code-sample.com'},
        {'name': 'Alok Singh', 'Age': 35, 'blog' : 'https://code-sample.com'},
        {'name': 'Dilip Singh', 'Age': 34, 'blog' : 'https://code-sample.com'}
      ]
  }

  getProject(): void {
    this.projectservice.getProjects()
    .subscribe(data => {
      this.projectList = data
    })
  }

  getCustumer(): void {
   this.custumerService.getCustomer()
    .subscribe(data2 => {
      this.custumerList = data2
    })
  }

  getQuotation(): void {
    this.quotationservice.getQuotation()
      .subscribe(data3 => {
        this.quotations = data3
      });
  }

  createQuotation(): void {
    this.createQuotationModel.show();
  }

  detailQuotation(quotation: Quotation): void {
    this.detailQuotationModel.show(quotation.id);
  }

  updateTable(data) {
    this.quotationservice.getQuotation()
    .subscribe(datas => this.quotations = datas);
  }

  transform(items: any, filter: any, defaultFilter: boolean): any {
    if (!filter) {
      return items;
    }

    if (!Array.isArray(items)) {
      return items;
    }

    if (filter && Array.isArray(items)) {
      const filterKeys = Object.keys(filter);

      if (defaultFilter) {
        return items.filter(item =>
            filterKeys.reduce((x, keyName) =>
                (x && new RegExp(filter[keyName], 'gi').test(item[keyName])) || filter[keyName] === '', true));
      } else {
        return items.filter(item => {
          return filterKeys.some((keyName) => {
            return new RegExp(filter[keyName], 'gi').test(item[keyName]) || filter[keyName] === '';
          });
        });
      }
    }
  }
}
