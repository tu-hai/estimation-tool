import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filCus'
})
export class SktFilterPipeCus implements PipeTransform {

  transform(value: any, filter?: any): any {
    if (filter === undefined) {
     return value;
    }
    return value.filter(
      item => (item.customerId.name.toLowerCase().indexOf(filter.toLowerCase()) > -1 )
   );

  }
}
