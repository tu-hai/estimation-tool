import { Component, ViewChild, ElementRef, OnInit } from '@angular/core';
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

  custumers: Customer[];
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

    onSubmit() {
      this.custumerService.editCustumer(this.editForm.value)
        .subscribe( data => {
          console.log('data', data);
          abp.notify.info('Updated Successfully');
          this.close();
        });
    }
}
