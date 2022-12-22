import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'imob',
        data: { pageTitle: 'imoReviewApp.imob.home.title' },
        loadChildren: () => import('./imob/imob.module').then(m => m.ImobModule),
      },
      {
        path: 'review',
        data: { pageTitle: 'imoReviewApp.review.home.title' },
        loadChildren: () => import('./review/review.module').then(m => m.ReviewModule),
      },
      {
        path: 'sub',
        data: { pageTitle: 'imoReviewApp.sub.home.title' },
        loadChildren: () => import('./sub/sub.module').then(m => m.SubModule),
      },
      {
        path: 'payment',
        data: { pageTitle: 'imoReviewApp.payment.home.title' },
        loadChildren: () => import('./payment/payment.module').then(m => m.PaymentModule),
      },
      {
        path: 'attachment',
        data: { pageTitle: 'imoReviewApp.attachment.home.title' },
        loadChildren: () => import('./attachment/attachment.module').then(m => m.AttachmentModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
