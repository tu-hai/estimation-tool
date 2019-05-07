import { Component, OnInit, ViewChild, ElementRef, EventEmitter } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import { Quotation } from './../../../models/quotation.model';
import { QuotationService } from '@shared/service-proxies/service-proxies';
import { from } from 'rxjs';
import { FormBuilder, FormGroup, Validators, AbstractControl, FormControl } from '@angular/forms';
@Component({
  selector: 'app-detail-quotation',
  templateUrl: './detail-quotation.component.html',
  styleUrls: ['./detail-quotation.component.css']
})
export class DetailQuotationComponent implements OnInit {
  quotations: Quotation[];
  quoctationById: any;
  addForm: FormGroup;

  active = false;
  @ViewChild('detailQuotationModel') modal: ModalDirective;
  @ViewChild('modalContent') modalContent: ElementRef;

  constructor(
    private quotationService: QuotationService,
    private formBuilder: FormBuilder,
    ) { }

  ngOnInit() {
    this.close();
    this.getQuotation();

    this.addForm = this.formBuilder.group({
      customerId: [''],
      subbject: [''],
      text: ['']
    })

  }

  getQuotation(): void {
    this.quotationService.getQuotation()
      .subscribe(data => {
        this.quotations = data
      });
  }

  onSubmit() {
    this.quotationService.sendMail(this.addForm.value)
        .subscribe(data => {
            abp.notify.info('Added Successfully');
            this.close();
        });
}

  show(id: number): void {
    this.quotationService.getQuoctationById(id).subscribe(data => {
      this.quoctationById = data;
      this.quoctationById = Array.of(this.quoctationById);
    });
    this.active = true;
    this.modal.show();
  }

  close(): void {
    this.active = false;
    this.modal.hide();
    console.log('an bui', this.quoctationById);
  }
}
