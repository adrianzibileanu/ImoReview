import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ImobComponent } from './list/imob.component';
import { ImobDetailComponent } from './detail/imob-detail.component';
import { ImobUpdateComponent } from './update/imob-update.component';
import { ImobDeleteDialogComponent } from './delete/imob-delete-dialog.component';
import { ImobRoutingModule } from './route/imob-routing.module';

@NgModule({
  imports: [SharedModule, ImobRoutingModule],
  declarations: [ImobComponent, ImobDetailComponent, ImobUpdateComponent, ImobDeleteDialogComponent],
})
export class ImobModule {}
