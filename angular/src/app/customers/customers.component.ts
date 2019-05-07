import { Component, OnInit, ViewChild } from '@angular/core';
import { appModuleAnimation } from '@shared/animations/routerTransition';
import { CustomerService } from '@shared/service-proxies/service-proxies';
import { Customer } from './../../models/customer.model';
import { CreateCustomersComponent } from 'app/customers/create-customers/create-customers.component';
import { EditCustomersComponent } from 'app/customers/edit-customers/edit-customers.component';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
@Component({
    selector: 'app-customers',
    templateUrl: './customers.component.html',
    animations: [appModuleAnimation()]
})
export class CustomersComponent implements OnInit {
    @ViewChild('createCustumerModal') createCustumerModal: CreateCustomersComponent;
    @ViewChild('editCustumerModal') editCustumerModal: EditCustomersComponent;

    customerList: Observable<any>;
    pages: Array<number>;
    totalItem: number;
    p = 1;
    itemsPerPage = 5;
    rotate = true;

    constructor(
        private customerService: CustomerService,
    ) {}

    setPage (i) {
        this.p = i;
        this.getCustumerBypage(this.p - 1);
    }
    ngOnInit() {
        this.setPage(this.p);
    }

    getCustumerBypage(i: number): void {
        this.customerList =  this.customerService.getCustumerbyPage(i).pipe(
            map(data => {
                this.totalItem = data.totalElements;
                return data.content;
            })
        );
    }


    createCustomer(): void {
        this.createCustumerModal.show();
    }

    editCustomer(customer: Customer): void {
        this.editCustumerModal.show(customer.id);
    }

    deleteCustumer(custumerId: Customer): void {
        abp.message.confirm(
            'Delete custumer \'' + custumerId.name + '\'?',
            (result: boolean) => {
                if (result) {
                    this.customerService.deleteCustumer(custumerId.id)
                        .subscribe(() => {
                            this.callBackTableData();
                            // this.customerList = this.customerList.filter(u => u.id !== custumerId.id);
                            abp.notify.info('Deleted: ' + custumerId.name);
                        });
                }
            }
        );
    }

    callBackTableData() {
        this.customerList =  this.customerService.getCustumerbyPage(this.p - 1).pipe(
          map(data => {
              this.totalItem = data.totalElements;
              return data.content;
          })
      );
  }
}
