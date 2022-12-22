import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SubComponent } from '../list/sub.component';
import { SubDetailComponent } from '../detail/sub-detail.component';
import { SubUpdateComponent } from '../update/sub-update.component';
import { SubRoutingResolveService } from './sub-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const subRoute: Routes = [
  {
    path: '',
    component: SubComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SubDetailComponent,
    resolve: {
      sub: SubRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SubUpdateComponent,
    resolve: {
      sub: SubRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SubUpdateComponent,
    resolve: {
      sub: SubRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(subRoute)],
  exports: [RouterModule],
})
export class SubRoutingModule {}
