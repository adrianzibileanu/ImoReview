import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ReviewFormService, ReviewFormGroup } from './review-form.service';
import { IReview } from '../review.model';
import { ReviewService } from '../service/review.service';
import { IImob } from 'app/entities/imob/imob.model';
import { ImobService } from 'app/entities/imob/service/imob.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-review-update',
  templateUrl: './review-update.component.html',
})
export class ReviewUpdateComponent implements OnInit {
  isSaving = false;
  review: IReview | null = null;

  imobsSharedCollection: IImob[] = [];
  usersSharedCollection: IUser[] = [];

  editForm: ReviewFormGroup = this.reviewFormService.createReviewFormGroup();

  constructor(
    protected reviewService: ReviewService,
    protected reviewFormService: ReviewFormService,
    protected imobService: ImobService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareImob = (o1: IImob | null, o2: IImob | null): boolean => this.imobService.compareImob(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ review }) => {
      this.review = review;
      if (review) {
        this.updateForm(review);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const review = this.reviewFormService.getReview(this.editForm);
    if (review.id !== null) {
      this.subscribeToSaveResponse(this.reviewService.update(review));
    } else {
      this.subscribeToSaveResponse(this.reviewService.create(review));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReview>>): void {
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

  protected updateForm(review: IReview): void {
    this.review = review;
    this.reviewFormService.resetForm(this.editForm, review);

    this.imobsSharedCollection = this.imobService.addImobToCollectionIfMissing<IImob>(this.imobsSharedCollection, review.imobID);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, review.userID);
  }

  protected loadRelationshipsOptions(): void {
    this.imobService
      .query()
      .pipe(map((res: HttpResponse<IImob[]>) => res.body ?? []))
      .pipe(map((imobs: IImob[]) => this.imobService.addImobToCollectionIfMissing<IImob>(imobs, this.review?.imobID)))
      .subscribe((imobs: IImob[]) => (this.imobsSharedCollection = imobs));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.review?.userID)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
