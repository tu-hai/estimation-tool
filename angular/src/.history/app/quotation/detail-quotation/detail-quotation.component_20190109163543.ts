import { Component, OnInit, ViewChild, ElementRef, EventEmitter} from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import { Quotation } from './../../../models/quotation.model';
import { QuotationService } from '@shared/service-proxies/service-proxies';
import { from } from 'rxjs';
@Component({
  selector: 'app-detail-quotation',
  templateUrl: './detail-quotation.component.html',
  styleUrls: ['./detail-quotation.component.css']
})
export class DetailQuotationComponent implements OnInit {
  quotation: Quotation[] = null;
  showOptionSelected: number;
  active = false;
  @ViewChild('detailQuotationModel') modal: ModalDirective;
  @ViewChild('modalContent') modalContent: ElementRef;

  constructor(private quotationService: QuotationService) { }
  ngOnInit() {
    this.close();
    this.getQuotation();

  }
  getQuotation(): void {
    this.quotationService.getQuotation()
    .subscribe(data => {
      this.quotation = data
      console.log('kkkkkkkkkkkkkkkk', data)
    });
  }
  show(id: number): void {
    this.quotationService.getQuoctationById(id).subscribe(data => {
      this.quotation = data;
    } );
    this.active = true;
    this.modal.show();
  }
  close(): void {
    this.active = false;
    this.modal.hide();
  }
}
