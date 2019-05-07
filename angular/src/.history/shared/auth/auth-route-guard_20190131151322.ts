import { Injectable } from '@angular/core';
import { PermissionCheckerService } from '@abp/auth/permission-checker.service';
import { AppSessionService } from '../session/app-session.service';
import { AuthenticateResultModel} from './../service-proxies/service-proxies'

import {
    CanActivate, Router,
    ActivatedRouteSnapshot,
    RouterStateSnapshot,
    CanActivateChild
} from '@angular/router';



@Injectable()
export class AppRouteGuard implements CanActivate, CanActivateChild {
    constructor(
        private _permissionChecker: PermissionCheckerService,
        private _router: Router,
        private _sessionService: AppSessionService,
    ) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        if (!this._sessionService.user || this._sessionService.user.id) {
            debugger
            this._router.navigate(['/account/login']);
            return false;
        }

        if (!route.data || !route.data['permission']) {
          debugger
            return true;
        }

        if (this._permissionChecker.isGranted(route.data['permission'])) {
            debugger
            return true;
        }

        this._router.navigate([this.selectBestRoute()]);
        return false;
    }

    canActivateChild(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        debugger
        return this.canActivate(route, state);
    }

    selectBestRoute(): string {
        if (!this._sessionService.user) {
            debugger
            return '/account/login';
        }
        if (this._permissionChecker.isGranted('Pages.Users')) {
            debugger
            return '/app/admin/users';
        }
        return '/app/home';
    }
}
