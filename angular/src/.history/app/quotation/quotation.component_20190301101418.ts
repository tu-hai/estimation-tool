import {OnInit } from '@angular/core';
import {CreateQuotationComponent } from './create-quotation/create-quotation.component';
import {EditQuotationComponent} from './edit-quotation/edit-quotation.component'
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
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-quotation',
  templateUrl: './quotation.component.html',
  styleUrls: ['./quotation.component.css'],
  animations: [appModuleAnimation()]
})


export class QuotationComponent  implements AfterViewInit {
  @ViewChild('createQuotationModel') createQuotationModel: CreateQuotationComponent;
  @ViewChild('detailQuotationModel') detailQuotationModel: DetailQuotationComponent;
  @ViewChild('editQuotationModel') editQuotationModel: EditQuotationComponent;

  public filterPrj = null;
  public filterCus = null;

  form: FormGroup;
  projectList: any;
  custumerList: Customer[];
  quotationList: Observable<any>;
  pages: Array<number>;
  totalItem: number;
  p = 1;
  itemsPerPage = 5;
  rotate = true;

  constructor( private quotationservice: QuotationService, private formBuilder: FormBuilder,
    private projectservice: ProjectService, private custumerService: CustomerService) {
        this.form = new FormGroup({
         project  : new FormControl(null)
    })
  };

  setPage (i) {
    this.p = i;
    this.getQuotationbyPages(this.p - 1);
  }


  ngAfterViewInit() {
     this.setPage(this.p);
      this.getProject();
      this.getCustumer();
      // this.getQuotation();
      this.form = this.formBuilder.group({
        name: [null, Validators.required],
        hours: [null, [Validators.required]],
      });
  }

  getQuotationbyPages(i: number): void {
    this.quotationList =  this.quotationservice.getQuotationPage(i).pipe(
        map(data => {
            this.totalItem = data.totalElements;
            return data.content;
        })
    );
  }




  getProject(): void {
    this.projectservice.getProjects()
    .subscribe(data => {
      this.projectList = data
    })
  }

  getCustumer(): void {
   this.custumerService.getCustomer()
    .subscribe(data => {
      this.custumerList = data
    })
  }

  createQuotation(): void {
    this.createQuotationModel.show();
  }

  editQuotation(quotation: Quotation): void {
    this.editQuotationModel.show(quotation.id);
  }

  detailQuotation(quotation: Quotation): void {
    this.detailQuotationModel.show(quotation.id);
  }

  delQuotation(quotation: Quotation): void {
    abp.message.confirm(
        'Delete Quotation \'' + quotation.projectId.name + '\'?',
        (result: boolean) => {
            if (result) {
                this.quotationservice.delQuotation(quotation.id)
                    .subscribe(() => {
                        // this.quotations = this.quotations.filter(u => u.id !== quotation.id);
                        this.callBackTableData();
                        abp.notify.info('Deleted: ' + quotation.projectId.name);
                    });
            }
        }
    );
 }

 callBackTableData() {
    this.quotationList =  this.quotationservice.getQuotationPage(this.p - 1).pipe(
      map(data => {
          this.totalItem = data.totalElements;
         return data.content;
     })
   );
  }
}
