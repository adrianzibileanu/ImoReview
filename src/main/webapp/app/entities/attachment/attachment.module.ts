import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AttachmentComponent } from './list/attachment.component';
import { AttachmentDetailComponent } from './detail/attachment-detail.component';
import { AttachmentUpdateComponent } from './update/attachment-update.component';
import { AttachmentDeleteDialogComponent } from './delete/attachment-delete-dialog.component';
import { AttachmentRoutingModule } from './route/attachment-routing.module';
import { AttachmentFilterPipe } from './list/attachmentFilter.pipe';

@NgModule({
  imports: [SharedModule, AttachmentRoutingModule],
  declarations: [
    AttachmentComponent,
    AttachmentDetailComponent,
    AttachmentUpdateComponent,
    AttachmentDeleteDialogComponent,
    AttachmentFilterPipe,
  ],
})
export class AttachmentModule {}
