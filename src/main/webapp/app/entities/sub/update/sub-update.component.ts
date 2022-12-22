import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SubFormService, SubFormGroup } from './sub-form.service';
import { ISub } from '../sub.model';
import { SubService } from '../service/sub.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { SubType } from 'app/entities/enumerations/sub-type.model';

@Component({
  selector: 'jhi-sub-update',
  templateUrl: './sub-update.component.html',
})
export class SubUpdateComponent implements OnInit {
  isSaving = false;
  sub: ISub | null = null;
  subTypeValues = Object.keys(SubType);

  usersSharedCollection: IUser[] = [];

  editForm: SubFormGroup = this.subFormService.createSubFormGroup();

  constructor(
    protected subService: SubService,
    protected subFormService: SubFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sub }) => {
      this.sub = sub;
      if (sub) {
        this.updateForm(sub);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sub = this.subFormService.getSub(this.editForm);
    if (sub.id !== null) {
      this.subscribeToSaveResponse(this.subService.update(sub));
    } else {
      this.subscribeToSaveResponse(this.subService.create(sub));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISub>>): void {
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

  protected updateForm(sub: ISub): void {
    this.sub = sub;
    this.subFormService.resetForm(this.editForm, sub);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, sub.userID);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.sub?.userID)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
