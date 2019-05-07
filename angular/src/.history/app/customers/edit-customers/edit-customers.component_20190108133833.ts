import { Component, ViewChild, Injector, Output, EventEmitter, ElementRef, OnInit } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { CustomerService } from '@shared/service-proxies/service-proxies';
@Component({
  selector: 'app-edit-customers',
  templateUrl: './edit-customers.component.html',
  styleUrls: ['./edit-customers.component.css']
})
export class EditCustomersComponent implements OnInit {
  @ViewChild('editCustumerModal') modal: ModalDirective;
  @ViewChild('modalContent') modalContent: ElementRef;

  active = false;
  addForm: FormGroup;
    constructor(   private formBuilder: FormBuilder,
      private custumerService: CustomerService) {

    }
    ngOnInit() {
      this.show();
      this.addForm = this.formBuilder.group({
        name: [''],
        country: [''],
      });
    }

    show(): void {
     this.active = true;
     this.modal.show();
    }
    close(): void {
     this.active = false;
     this.modal.hide();
    }
    save(): void {}

}
