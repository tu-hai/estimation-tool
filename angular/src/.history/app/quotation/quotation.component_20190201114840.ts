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


  quotations: Quotation[];
  projectList: Project[];
  custumerList: Customer[];
  public filter = '';

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
                        this.quotations = this.quotations.filter(u => u.id !== quotation.id);
                        abp.notify.info('Deleted: ' + quotation.projectId.name);
                    });
            }
        }
    );
 }

  updateTable(data) {
    this.quotationservice.getQuotation()
    .subscribe(datas => this.quotations = datas);
  }
}
