import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'Se'
})
export class SktFilterPipe implements PipeTransform {

  transform(value: any, args?: any): any {

    if ( !args ) {
      return value;
    } else {
      args = args.toUpperCase()
    }


     return value.filter(
       item => item.id.indexOf(args) > -1
    );
   }

}
