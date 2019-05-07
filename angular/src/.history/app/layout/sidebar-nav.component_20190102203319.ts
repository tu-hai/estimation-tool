import { Component, Injector, ViewEncapsulation } from '@angular/core';
import { AppComponentBase } from '@shared/app-component-base';
import { MenuItem } from '@shared/layout/menu-item';

@Component({
    templateUrl: './sidebar-nav.component.html',
    // tslint:disable-next-line:component-selector
    selector: 'sidebar-nav',
    encapsulation: ViewEncapsulation.None
})
export class SideBarNavComponent extends AppComponentBase {

    menuItems: MenuItem[] = [
        new MenuItem(this.l('HomePage'), '', 'home', '/app/home'),
        new MenuItem(this.l('Tenants'), 'Pages.Tenants', 'business', '/app/tenants'),
        new MenuItem(this.l('Users'), 'Page.Users', 'people', '/app/users'),
        new MenuItem(this.l('Customers'), '', 'people', '/app/customers'),
        new MenuItem(this.l('Roles'), 'Pages.Roles', 'local_offer', '/app/roles'),
        new MenuItem(this.l('Projects'), '', 'computer', '/app/projects'),
        new MenuItem(this.l('Quotation'), '', 'work', '/app/quotation'),
        new MenuItem (this.l('WBS'), '', 'assessment', '/app/wbs'),
        new MenuItem(this.l('About'), '', 'info', '/app/about'),
    ];

    constructor(
        injector: Injector
    ) {
        super(injector);
    }

    showMenuItem(menuItem): boolean {
        if (menuItem.permissionName) {
            return this.permission.isGranted(menuItem.permissionName);
        }

        return true;
    }
}
