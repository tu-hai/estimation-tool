import { Component, OnInit } from '@angular/core';
import { appModuleAnimation } from '@shared/animations/routerTransition';
import { HttpClient, HttpResponse, HttpResponseBase } from '@angular/common/http';
@Component({
  selector: 'app-quotation',
  templateUrl: './quotation.component.html',
  styleUrls: ['./quotation.component.css'],
  animations: [appModuleAnimation()]
})
export class QuotationComponent implements OnInit {
  private http: HttpClient;
  private baseUrl: string;
  constructor(http: HttpClient, baseUrl: string) {
    this.http = http;
    this.baseUrl = baseUrl ? baseUrl : '';
    console.log ('day la base url', baseUrl);
   }
   getProjects() {
    let url_ = this.baseUrl + '/api/getAllProject';
    url_ = url_.replace(/[?&]$/, "");

   }
  ngOnInit() {
  }

}
