import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SubComponent } from './list/sub.component';
import { SubDetailComponent } from './detail/sub-detail.component';
import { SubUpdateComponent } from './update/sub-update.component';
import { SubDeleteDialogComponent } from './delete/sub-delete-dialog.component';
import { SubRoutingModule } from './route/sub-routing.module';

@NgModule({
  imports: [SharedModule, SubRoutingModule],
  declarations: [SubComponent, SubDetailComponent, SubUpdateComponent, SubDeleteDialogComponent],
})
export class SubModule {}
