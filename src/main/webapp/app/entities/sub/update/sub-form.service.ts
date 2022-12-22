import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISub, NewSub } from '../sub.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISub for edit and NewSubFormGroupInput for create.
 */
type SubFormGroupInput = ISub | PartialWithRequiredKeyOf<NewSub>;

type SubFormDefaults = Pick<NewSub, 'id' | 'subscribed' | 'active'>;

type SubFormGroupContent = {
  id: FormControl<ISub['id'] | NewSub['id']>;
  subscribed: FormControl<ISub['subscribed']>;
  active: FormControl<ISub['active']>;
  expirationDate: FormControl<ISub['expirationDate']>;
  type: FormControl<ISub['type']>;
  userID: FormControl<ISub['userID']>;
};

export type SubFormGroup = FormGroup<SubFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SubFormService {
  createSubFormGroup(sub: SubFormGroupInput = { id: null }): SubFormGroup {
    const subRawValue = {
      ...this.getFormDefaults(),
      ...sub,
    };
    return new FormGroup<SubFormGroupContent>({
      id: new FormControl(
        { value: subRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      subscribed: new FormControl(subRawValue.subscribed),
      active: new FormControl(subRawValue.active),
      expirationDate: new FormControl(subRawValue.expirationDate),
      type: new FormControl(subRawValue.type),
      userID: new FormControl(subRawValue.userID),
    });
  }

  getSub(form: SubFormGroup): ISub | NewSub {
    return form.getRawValue() as ISub | NewSub;
  }

  resetForm(form: SubFormGroup, sub: SubFormGroupInput): void {
    const subRawValue = { ...this.getFormDefaults(), ...sub };
    form.reset(
      {
        ...subRawValue,
        id: { value: subRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SubFormDefaults {
    return {
      id: null,
      subscribed: false,
      active: false,
    };
  }
}
