import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filProject'
})
export class SktFilterPipe implements PipeTransform {
  transform(value: any, filterPrj?: any): any {
    if (filterPrj === undefined || filterPrj == null || filterPrj == '') {
     return value;
    }
    return value.filter(
      item => (item.projectId.name.toLowerCase().indexOf(filterPrj.toLowerCase()) > -1 )
   );

  }
}

@Pipe({
  name: 'filCustumer'
})
export class SktFilterPipeCus implements PipeTransform {
  transform(value: any, filterCus?: any): any {
    if (filterCus === undefined) {
     return value;
    }
    return value.filter(
      item => (item.customerId.name.toLowerCase().indexOf(filterCus.toLowerCase()) > -1 )
   );

  }
}

