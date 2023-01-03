import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IReview, NewReview } from '../review.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReview for edit and NewReviewFormGroupInput for create.
 */
type ReviewFormGroupInput = IReview | PartialWithRequiredKeyOf<NewReview>;

type ReviewFormDefaults = Pick<NewReview, 'id'>;

type ReviewFormGroupContent = {
  id: FormControl<IReview['id'] | NewReview['id']>;
  title: FormControl<IReview['title']>;
  body: FormControl<IReview['body']>;
  rating: FormControl<IReview['rating']>;
  userID: FormControl<IReview['userID']>;
};

export type ReviewFormGroup = FormGroup<ReviewFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReviewFormService {
  createReviewFormGroup(review: ReviewFormGroupInput = { id: null }): ReviewFormGroup {
    const reviewRawValue = {
      ...this.getFormDefaults(),
      ...review,
    };
    return new FormGroup<ReviewFormGroupContent>({
      id: new FormControl(
        { value: reviewRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(reviewRawValue.title, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(20)],
      }),
      body: new FormControl(reviewRawValue.body, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(800)],
      }),
      rating: new FormControl(reviewRawValue.rating, {
        validators: [Validators.required],
      }),
      userID: new FormControl(reviewRawValue.userID),
    });
  }

  getReview(form: ReviewFormGroup): IReview | NewReview {
    return form.getRawValue() as IReview | NewReview;
  }

  resetForm(form: ReviewFormGroup, review: ReviewFormGroupInput): void {
    const reviewRawValue = { ...this.getFormDefaults(), ...review };
    form.reset(
      {
        ...reviewRawValue,
        id: { value: reviewRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ReviewFormDefaults {
    return {
      id: null,
    };
  }
}
