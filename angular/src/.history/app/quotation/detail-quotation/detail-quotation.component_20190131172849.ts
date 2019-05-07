import { Component, OnInit, ViewChild, ElementRef, EventEmitter } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import { Quotation, DetailQuotation, ListPDF } from './../../../models/quotation.model';
import { QuotationService } from '@shared/service-proxies/service-proxies';
import { FormBuilder, FormGroup, Validators, AbstractControl, FormControl } from '@angular/forms';
@Component({
  selector: 'app-detail-quotation',
  templateUrl: './detail-quotation.component.html',
  styleUrls: ['./detail-quotation.component.css']
})
export class DetailQuotationComponent implements OnInit {
  quoctationById: any;
  nhubui: number;
  nhubui2: number;
  addForm: FormGroup;
  listPDF: ListPDF;
  linkDownloadPDF: string;

  active = false;
  @ViewChild('detailQuotationModel') modal: ModalDirective;
  @ViewChild('modalContent') modalContent: ElementRef;

  constructor(
    private quotationService: QuotationService,
    private formBuilder: FormBuilder,
    ) { }

  ngOnInit() {
    this.close();
    this.addForm = this.formBuilder.group({
      customerId: [''],
      quotationId: [''],
      subbject: [''],
      text: ['']
    })

  }

  onSubmit() {
    this.quotationService.sendMail(this.addForm.value)
        .subscribe(data => {
            abp.notify.info('Send Successfully');
        });
  }

  show(id: number): void {
    this.quotationService.getQuoctationById(id).subscribe(data => {
      this.quoctationById = data;
      this.nhubui = data.customer;
      this.nhubui2 = data.id;
      this.quoctationById = Array.of(this.quoctationById);
    });


    this.active = true;
    this.modal.show();
  }

  showListPDF(id: number) {
    this.quotationService.getListPDF(id).subscribe(data => {
      this.listPDF = data
    })
  }

  downloadPDF(id: number) {
    this.quotationService.downloadPDF(id).subscribe(data => {

    })
  }

  close(): void {
    this.active = false;
    this.modal.hide();
  }
}
