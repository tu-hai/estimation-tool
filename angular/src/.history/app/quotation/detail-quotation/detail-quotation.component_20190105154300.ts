import { Component, OnInit, ViewChild, ElementRef, EventEmitter} from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import { Quotation } from './../../../models/quotation.model';
import { QuotationService } from '@shared/service-proxies/service-proxies';
@Component({
  selector: 'app-detail-quotation',
  templateUrl: './detail-quotation.component.html',
  styleUrls: ['./detail-quotation.component.css']
})
export class DetailQuotationComponent implements OnInit {
  quotation: Quotation[];
  constructor(private quotationService: QuotationService) { }

  ngOnInit() {
    this.close();
  }
  close(): void {
    this.active = false;
    this.modal.hide();
  }
}
