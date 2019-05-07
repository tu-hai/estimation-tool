import { NgModule, Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AppComponent } from './app.component';
import { AppRouteGuard } from '@shared/auth/auth-route-guard';
import { HomeComponent } from './home/home.component';
import { AboutComponent } from './about/about.component';
import { UsersComponent } from './users/users.component';
import { TenantsComponent } from './tenants/tenants.component';
import { RolesComponent } from 'app/roles/roles.component';
import { CustomersComponent } from 'app/customers/customers.component';
import { from } from 'rxjs';
import { QuotationComponent } from './quotation/quotation.component';
import { ProjectsComponent } from './projects/projects.component';
import { WBSComponent } from './wbs/wbs.component'

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: '',
                component: AppComponent,
                children: [
                    { path: 'home', component: HomeComponent,  canActivate: [AppRouteGuard] },
                    { path: 'users', component: UsersComponent, canActivate: [AppRouteGuard] },
                    { path: 'customers', component: CustomersComponent, canActivate: [AppRouteGuard] },
                    { path: 'roles', component: RolesComponent, data: { permission: 'Pages.Roles' }, canActivate: [AppRouteGuard] },
                    { path: 'tenants', component: TenantsComponent, data: { permission: 'Pages.Tenants' }, canActivate: [AppRouteGuard] },
                    { path: 'about', component: AboutComponent },
                    { path: 'quotation', component: QuotationComponent},
                    { path: 'projects', component: ProjectsComponent},
                ]
            }
        ])
    ],
    exports: [RouterModule]
})
export class AppRoutingModule { }
