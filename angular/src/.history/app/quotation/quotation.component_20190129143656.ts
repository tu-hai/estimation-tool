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
import {DetailQuotationComponent} from './detail-quotation/detail-quotation.component';
import {MatTableDataSource} from '@angular/material';

@Component({
  selector: 'app-quotation',
  templateUrl: './quotation.component.html',
  styleUrls: ['./quotation.component.css'],
  animations: [appModuleAnimation()]
})


export class QuotationComponent  implements AfterViewInit {
  @ViewChild('createQuotationModel') createQuotationModel: CreateQuotationComponent;
  @ViewChild('detailQuotationModel') detailQuotationModel: DetailQuotationComponent;

  displayedColumns = ['position', 'name', 'weight', 'symbol'];
  dataSource = new MatTableDataSource(ELEMENT_DATA);
  

  quotations: Quotation[];
  projectList: Project[];
  custumerList: Customer[];


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
      // this.myFunction();
      this.getProject();
      this.getCustumer();
      this.getQuotation();
      this.form = this.formBuilder.group({
        name: [null, Validators.required],
        hours: [null, [Validators.required]],
      });

  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // MatTableDataSource defaults to lowercase matches
    this.dataSource.filter = filterValue;
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
        console.log('nhu bui', this.quotations)
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

}
export interface Element {
  name: string;
  position: number;
  weight: number;
  symbol: string;
}
const ELEMENT_DATA: Element[] = [
  {position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H'},
  {position: 2, name: 'Helium', weight: 4.0026, symbol: 'He'},
  {position: 3, name: 'Lithium', weight: 6.941, symbol: 'Li'},
  {position: 4, name: 'Beryllium', weight: 9.0122, symbol: 'Be'},
  {position: 5, name: 'Boron', weight: 10.811, symbol: 'B'},
  {position: 6, name: 'Carbon', weight: 12.0107, symbol: 'C'},
  {position: 7, name: 'Nitrogen', weight: 14.0067, symbol: 'N'},
  {position: 8, name: 'Oxygen', weight: 15.9994, symbol: 'O'},
  {position: 9, name: 'Fluorine', weight: 18.9984, symbol: 'F'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  {position: 11, name: 'Sodium', weight: 22.9897, symbol: 'Na'},
  {position: 12, name: 'Magnesium', weight: 24.305, symbol: 'Mg'},
  {position: 13, name: 'Aluminum', weight: 26.9815, symbol: 'Al'},
  {position: 14, name: 'Silicon', weight: 28.0855, symbol: 'Si'},
  {position: 15, name: 'Phosphorus', weight: 30.9738, symbol: 'P'},
  {position: 16, name: 'Sulfur', weight: 32.065, symbol: 'S'},
  {position: 17, name: 'Chlorine', weight: 35.453, symbol: 'Cl'},
  {position: 18, name: 'Argon', weight: 39.948, symbol: 'Ar'},
  {position: 19, name: 'Potassium', weight: 39.0983, symbol: 'K'},
  {position: 20, name: 'Calcium', weight: 40.078, symbol: 'Ca'},
];