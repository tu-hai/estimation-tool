import { Component, OnInit, ViewChild, ElementRef, EventEmitter } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import { Quotation, DetailQuotation } from './../../../models/quotation.model';
import { QuotationService } from '@shared/service-proxies/service-proxies';
import { from } from 'rxjs';
import { FormBuilder, FormGroup, Validators, AbstractControl, FormControl } from '@angular/forms';
@Component({
  selector: 'app-detail-quotation',
  templateUrl: './detail-quotation.component.html',
  styleUrls: ['./detail-quotation.component.css']
})
export class DetailQuotationComponent implements OnInit {
  // quotations: Quotation[];
  quoctationById: DetailQuotation;
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
    });
    this.active = true;
    this.modal.show();
  }

  close(): void {
    this.active = false;
    this.modal.hide();
    console.log('nhu buiiii', this.quoctationById)
  }
}
