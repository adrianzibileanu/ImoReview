import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ImobFormService, ImobFormGroup } from './imob-form.service';
import { IImob } from '../imob.model';
import { ImobService } from '../service/imob.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ImobType } from 'app/entities/enumerations/imob-type.model';
import { ImobCateg } from 'app/entities/enumerations/imob-categ.model';
import { ImobServ } from 'app/entities/enumerations/imob-serv.model';
import { Currency } from 'app/entities/enumerations/currency.model';

@Component({
  selector: 'jhi-imob-update',
  templateUrl: './imob-update.component.html',
})
export class ImobUpdateComponent implements OnInit {
  isSaving = false;
  imob: IImob | null = null;
  imobTypeValues = Object.keys(ImobType);
  imobCategValues = Object.keys(ImobCateg);
  imobServValues = Object.keys(ImobServ);
  currencyValues = Object.keys(Currency);

  usersSharedCollection: IUser[] = [];

  editForm: ImobFormGroup = this.imobFormService.createImobFormGroup();

  constructor(
    protected imobService: ImobService,
    protected imobFormService: ImobFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ imob }) => {
      this.imob = imob;
      if (imob) {
        this.updateForm(imob);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const imob = this.imobFormService.getImob(this.editForm);
    if (imob.id !== null) {
      this.subscribeToSaveResponse(this.imobService.update(imob));
    } else {
      this.subscribeToSaveResponse(this.imobService.create(imob));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImob>>): void {
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

  protected updateForm(imob: IImob): void {
    this.imob = imob;
    this.imobFormService.resetForm(this.editForm, imob);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, imob.imobstouser);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.imob?.imobstouser)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
