import { Component, OnInit, ViewChild } from '@angular/core';
import { appModuleAnimation } from '@shared/animations/routerTransition';
import { CustomerService } from '@shared/service-proxies/service-proxies';
import { Customer } from './../../models/customer.model';
import { CreateCustomersComponent } from 'app/customers/create-customers/create-customers.component';
import { EditCustomersComponent } from 'app/customers/edit-customers/edit-customers.component';
@Component({
    selector: 'app-customers',
    templateUrl: './customers.component.html',
    animations: [appModuleAnimation()]
})
export class CustomersComponent implements OnInit {
    @ViewChild('createCustumerModal') createCustumerModal: CreateCustomersComponent;
    @ViewChild('editCustumerModal') editCustumerModal: EditCustomersComponent;

    customerList: Customer[];

    constructor(
        private customerService: CustomerService,
    ) {

    }
    ngOnInit() {
        this.getCustomerFromServices();
    }


    getCustomerFromServices(): void {
        this.customerService.getCustomer()
            .subscribe(data => {
                this.customerList = data;
            });
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
                            this.customerList = this.customerList.filter(u => u.id !== custumerId.id);
                            abp.notify.info('Deleted: ' + custumerId.name);
                        });
                }
            }
        );
    }

    getValue(data) {
        this.customerList.push(data);
    }
    getValueEdit(data) {
        this.customerService.getCustomer()
            .subscribe(datas => {
                this.customerList = datas;
            });
    }
}
