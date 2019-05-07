import { Component, OnInit } from '@angular/core';
import { appModuleAnimation } from '@shared/animations/routerTransition';
import { HttpClient, HttpResponse, } from '@angular/common/http';
import { Injectable, Inject , Optional } from '@angular/core';
import { API_BASE_URL } from './../../shared/service-proxies/service-proxies'


@Component({
  selector: 'app-quotation',
  templateUrl: './quotation.component.html',
  styleUrls: ['./quotation.component.css'],
  animations: [appModuleAnimation()]
})

export class QuotationComponent implements OnInit {
    private http: HttpClient;
    private baseUrl: string;
  constructor(http: HttpClient, @Optional() @Inject(API_BASE_URL) baseUrl?: string) { 
  }

  getProjects (){
    let url_ = this.baseUrl + 'api/getProjects';
    url_ = url_.replace(/[?&]$/, "");
  }
  ngOnInit() {
  }

}
