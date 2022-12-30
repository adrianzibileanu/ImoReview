import { Pipe, PipeTransform } from '@angular/core';

import { IAttachment, NewAttachment } from '../attachment.model';

@Pipe({
  name: 'attachmentFilter',
  pure: false,
})
export class AttachmentFilterPipe implements PipeTransform {
  transform(items: any[], filter: any): any {
    if (!items || !filter) {
      return items;
    }
    // filter items array, items which match and return true will be
    // kept, false will be filtered out
    return items.filter(item => item.manytoone.login.indexOf(filter.manytoone?.login) !== -1);
  }
}
