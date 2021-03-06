import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filProject'
})
export class SktFilterPipe implements PipeTransform {

  transform(value: any, filter?: any): any {
    if (filter === undefined) {
     return value;
    }
    return value.filter(
      item => (item.projectId.name.toLowerCase().indexOf(filter.toLowerCase()) > -1 )
   );

  }
}
