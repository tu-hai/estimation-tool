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
  // tslint:disable-next-line:no-output-rename
  @Output('value') FV = new EventEmitter();

  @ViewChild('editCustumerModal') modal: ModalDirective;
  @ViewChild('modalContent') modalContent: ElementRef;
  // tslint:disable-next-line:no-output-rename
  @Output('value') FVe = new EventEmitter();

  customerList: Customer[] = null;
  active = false;
  editForm: FormGroup;

    constructor(
      private formBuilder: FormBuilder,
        private customerService: CustomerService,
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
      this.customerService.getCustomerById(id).subscribe(data => {
        this.customerList = data;
      } );
      this.active = true;
      this.modal.show();
    }

    close(): void {
     this.active = false;
     this.modal.hide();
    }

    onSubmit() {
      this.customerService.editCustumer(this.editForm.value)
        .subscribe( data => {
          this.FVe.emit(data);
          abp.notify.info('Updated Successfully');
          this.close();
        });
    }
}
