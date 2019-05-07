import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { JsonpModule } from '@angular/http';
import { HttpClientModule, HttpResponse } from '@angular/common/http';

import { ModalModule } from 'ngx-bootstrap';
import { NgxPaginationModule } from 'ngx-pagination';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { AbpModule } from '@abp/abp.module';

import { ServiceProxyModule } from '@shared/service-proxies/service-proxy.module';
import { SharedModule } from '@shared/shared.module';

import { HomeComponent } from '@app/home/home.component';
import { AboutComponent } from '@app/about/about.component';
import { UsersComponent } from '@app/users/users.component';
import { CreateUserComponent } from '@app/users/create-user/create-user.component';
import { EditUserComponent } from './users/edit-user/edit-user.component';
import { RolesComponent } from '@app/roles/roles.component';
import { CreateRoleComponent } from '@app/roles/create-role/create-role.component';
import { EditRoleComponent } from './roles/edit-role/edit-role.component';
import { TenantsComponent } from '@app/tenants/tenants.component';
import { CreateTenantComponent } from './tenants/create-tenant/create-tenant.component';
import { EditTenantComponent } from './tenants/edit-tenant/edit-tenant.component';
import { TopBarComponent } from '@app/layout/topbar.component';
import { TopBarLanguageSwitchComponent } from '@app/layout/topbar-languageswitch.component';
import { SideBarUserAreaComponent } from '@app/layout/sidebar-user-area.component';
import { SideBarNavComponent } from '@app/layout/sidebar-nav.component';
import { SideBarFooterComponent } from '@app/layout/sidebar-footer.component';
import { RightSideBarComponent } from '@app/layout/right-sidebar.component';
import { CustomersComponent } from './customers/customers.component';
import { CreateCustomersComponent } from './customers/create-customers/create-customers.component';
import { EditCustomersComponent } from './customers/edit-customers/edit-customers.component';
import { QuotationComponent } from './quotation/quotation.component';
import { CreateQuotationComponent } from './quotation/create-quotation/create-quotation.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { ProjectsComponent } from './projects/projects.component';
import { CreateProjectComponent } from './projects/create-project/create-project.component';
import { EditProjectComponent } from './projects/edit-project/edit-project.component';
import { WBSComponent } from './wbs/wbs.component';
import { DetailQuotationComponent } from './quotation/detail-quotation/detail-quotation.component';
import { MatTableModule } from '@angular/material';
import { MatPaginatorModule } from '@angular/material';
import { MatFormFieldModule, MatInputModule, MatSelectModule } from '@angular/material';
import {CdkTableModule} from '@angular/cdk/table';
import {CdkTreeModule} from '@angular/cdk/tree';
import {MatAutocompleteModule } from '@angular/material/autocomplete'
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';


@NgModule({
    declarations: [
        AppComponent,
        HomeComponent,
        AboutComponent,
        TenantsComponent,
CreateTenantComponent,
EditTenantComponent,
        UsersComponent,
CreateUserComponent,
EditUserComponent,
RolesComponent,
CreateRoleComponent,
EditRoleComponent,
        TopBarComponent,
        TopBarLanguageSwitchComponent,
        SideBarUserAreaComponent,
        SideBarNavComponent,
        SideBarFooterComponent,
        RightSideBarComponent,
        CustomersComponent,
        CreateCustomersComponent,
        EditCustomersComponent,
        QuotationComponent,
        CreateQuotationComponent,
        ProjectsComponent,
        CreateProjectComponent,
        EditProjectComponent,
        WBSComponent,
        DetailQuotationComponent,
    ],
    imports: [
        ReactiveFormsModule,
        CommonModule,
        FormsModule,
        HttpClientModule,
        JsonpModule,
        ModalModule.forRoot(),
        AbpModule,
        AppRoutingModule,
        ServiceProxyModule,
        SharedModule,
        NgxPaginationModule,
        HttpModule,
        HttpClientModule,
        MatTableModule,
        MatPaginatorModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatInputModule,
        MatPaginatorModule,
        MatSelectModule,
        MatTableModule,
        MatAutocompleteModule,
        MatInputModule,
        ReactiveFormsModule,
        MatAutocompleteModule,
        BrowserAnimationsModule,
        FormsModule,
        HttpModule
    ],
    providers: [

    ]
})
export class AppModule { }
