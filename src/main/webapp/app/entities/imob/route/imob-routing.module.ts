import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ImobComponent } from '../list/imob.component';
import { ImobDetailComponent } from '../detail/imob-detail.component';
import { ImobUpdateComponent } from '../update/imob-update.component';
import { ImobRoutingResolveService } from './imob-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const imobRoute: Routes = [
  {
    path: '',
    component: ImobComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ImobDetailComponent,
    resolve: {
      imob: ImobRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ImobUpdateComponent,
    resolve: {
      imob: ImobRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ImobUpdateComponent,
    resolve: {
      imob: ImobRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(imobRoute)],
  exports: [RouterModule],
})
export class ImobRoutingModule {}
