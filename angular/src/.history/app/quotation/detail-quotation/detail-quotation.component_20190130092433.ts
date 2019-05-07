import { Component, OnInit, ViewChild, ElementRef, EventEmitter } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import { Quotation, DetailQuotation, ExportPDF } from './../../../models/quotation.model';
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
  addForm: FormGroup;
  pdf: ExportPDF;

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
      this.quoctationById = Array.of(this.quoctationById);
    });

  this.quotationService.exportPDF().subscribe(data => {
    this.pdf = data
  })

    this.active = true;
    this.modal.show();
  }
  download() {
    window.open('http://' + this.pdf.wbs);
  }
  close(): void {
    this.active = false;
    this.modal.hide();
  }
}
