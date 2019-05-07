import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'sktFilter'
})
export class SktFilterPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    if (!args) {
     return value;
    }
    return value.filter(
      item => item.projectId.name.toLowerCase().indexOf(args.toLowerCase()) > -1
   );
  }
}
