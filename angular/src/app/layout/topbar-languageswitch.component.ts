import { Component, OnInit, Injector, ViewEncapsulation } from '@angular/core';
import { AppComponentBase } from '@shared/app-component-base';
import { UserServiceProxy, ChangeUserLanguageDto } from '@shared/service-proxies/service-proxies';

import * as _ from 'lodash';

@Component({
  templateUrl: './topbar-languageswitch.component.html',
  selector: 'topbar-languageswitch',
  encapsulation: ViewEncapsulation.None
})
export class TopBarLanguageSwitchComponent extends AppComponentBase implements OnInit {

  languages: abp.localization.ILanguageInfo[];
  currentLanguage: abp.localization.ILanguageInfo;
  constructor(
      injector: Injector,
      private _userService: UserServiceProxy
  ) {
    super(injector);
  }

  ngOnInit() {
    this.languages = _.filter(this.localization.languages, l => !l.isDisabled);
    this.currentLanguage = this.localization.currentLanguage;
  }


}
