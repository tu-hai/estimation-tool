import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'Fillter'
})
export class SktFilterPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    value = value.filter(function(search) {
      return search.Name.toLowerCase().indexOf(args.toLowerCase()) > -1;
    });
    return value;
  }

}
