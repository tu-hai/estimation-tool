import { Component, Injector, ViewEncapsulation, OnInit, Optional } from '@angular/core';
import { AppComponentBase } from '@shared/app-component-base';
import { MenuItem } from '@shared/layout/menu-item';

@Component({
    templateUrl: './sidebar-nav.component.html',
    // tslint:disable-next-line:component-selector
    selector: 'sidebar-nav',
    encapsulation: ViewEncapsulation.None
})
export class SideBarNavComponent extends AppComponentBase implements OnInit {
    shownLoginName = '';
    userRole: boolean;

    
    menuItems: MenuItem[] = [
        new MenuItem(this.l('HomePage'), '', 'home', '/app/home'),
        new MenuItem(this.l('Customers'), '', 'people', '/app/customers'),
        new MenuItem(this.l('Projects'), '', 'computer', '/app/projects'),
        new MenuItem(this.l('Quotation'), '', 'work', '/app/quotation'),
        new MenuItem(this.l('About'), '', 'info', '/app/about'),
        new MenuItem(this.l('Roles'), 'locked', 'local_offer', '/app/roles'),
        new MenuItem(this.l('Users'), 'locked', 'people', '/app/users'),
    ];
    
   
    
    constructor(
        injector: Injector
    ) {
        super(injector);
    }
    ngOnInit() {
        this.shownLoginName = this.appSession.getShownLoginName();  
       if (this.shownLoginName == 'developer') {
         var remove = this.menuItems.splice(1,2);     
         this.menuItems = this.menuItems;
       }  
    }
    showMenuItem(menuItem): boolean {
        if (menuItem.permissionName) {
            return this.permission.isGranted(menuItem.permissionName)
            // return false
        }
        return true;
    }
}
