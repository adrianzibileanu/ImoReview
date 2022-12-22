import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PaymentFormService, PaymentFormGroup } from './payment-form.service';
import { IPayment } from '../payment.model';
import { PaymentService } from '../service/payment.service';
import { ISub } from 'app/entities/sub/sub.model';
import { SubService } from 'app/entities/sub/service/sub.service';
import { Currency } from 'app/entities/enumerations/currency.model';

@Component({
  selector: 'jhi-payment-update',
  templateUrl: './payment-update.component.html',
})
export class PaymentUpdateComponent implements OnInit {
  isSaving = false;
  payment: IPayment | null = null;
  currencyValues = Object.keys(Currency);

  subsSharedCollection: ISub[] = [];

  editForm: PaymentFormGroup = this.paymentFormService.createPaymentFormGroup();

  constructor(
    protected paymentService: PaymentService,
    protected paymentFormService: PaymentFormService,
    protected subService: SubService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSub = (o1: ISub | null, o2: ISub | null): boolean => this.subService.compareSub(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payment }) => {
      this.payment = payment;
      if (payment) {
        this.updateForm(payment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const payment = this.paymentFormService.getPayment(this.editForm);
    if (payment.id !== null) {
      this.subscribeToSaveResponse(this.paymentService.update(payment));
    } else {
      this.subscribeToSaveResponse(this.paymentService.create(payment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(payment: IPayment): void {
    this.payment = payment;
    this.paymentFormService.resetForm(this.editForm, payment);

    this.subsSharedCollection = this.subService.addSubToCollectionIfMissing<ISub>(this.subsSharedCollection, payment.subID);
  }

  protected loadRelationshipsOptions(): void {
    this.subService
      .query()
      .pipe(map((res: HttpResponse<ISub[]>) => res.body ?? []))
      .pipe(map((subs: ISub[]) => this.subService.addSubToCollectionIfMissing<ISub>(subs, this.payment?.subID)))
      .subscribe((subs: ISub[]) => (this.subsSharedCollection = subs));
  }
}
