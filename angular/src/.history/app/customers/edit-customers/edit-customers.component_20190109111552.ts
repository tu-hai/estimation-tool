import { Component, ViewChild, ElementRef, Output, OnInit, EventEmitter } from '@angular/core';
import { ModalDirective } from 'ngx-bootstrap';
import {FormBuilder, FormGroup} from '@angular/forms';
import { CustomerService } from '@shared/service-proxies/service-proxies';
import { Customer} from './../../../models/customer.model'

@Component({
  selector: 'app-edit-customers',
  templateUrl: './edit-customers.component.html',
  styleUrls: ['./edit-customers.component.css']
})
export class EditCustomersComponent implements OnInit {
  @ViewChild('editCustumerModal') modal: ModalDirective;
  @ViewChild('modalContent') modalContent: ElementRef;


  custumerList: Customer[] = null;
  active = false;
  editForm: FormGroup;

    constructor(
      private formBuilder: FormBuilder,
        private custumerService: CustomerService
      ) {

    }
    ngOnInit() {
      this.editForm = this.formBuilder.group({
        id: [''],
        name: [''],
        country: [''],
        company: [''],
        tel: [''],
        email: [''],
        billaddress: [''],
        skypeid: [''],
      });
    }

    show(id: number): void {
      this.custumerService.getCustomerById(id)
      .subscribe(
      (result) => {
          this.custumerList = result;
          this.active = true;
          this.modal.show();
      });
      console.log('blala liscocat', this.custumerList)
    }
    close(): void {
     this.active = false;
     this.modal.hide();
    }

    onSubmit() {
      this.custumerService.editCustumer(this.editForm.value)
        .subscribe( data => {
          abp.notify.info('Updated Successfully');
          this.close();
        });
    }
}
