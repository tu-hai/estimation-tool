import { Component, OnInit } from '@angular/core';
import { appModuleAnimation } from '@shared/animations/routerTransition';
@Component({
  selector: 'app-quotation',
  templateUrl: './quotation.component.html',
  styleUrls: ['./quotation.component.css'],
  animations: [appModuleAnimation()]
})
export class QuotationComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
