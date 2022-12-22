import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IImob, NewImob } from '../imob.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IImob for edit and NewImobFormGroupInput for create.
 */
type ImobFormGroupInput = IImob | PartialWithRequiredKeyOf<NewImob>;

type ImobFormDefaults = Pick<NewImob, 'id'>;

type ImobFormGroupContent = {
  id: FormControl<IImob['id'] | NewImob['id']>;
  title: FormControl<IImob['title']>;
  description: FormControl<IImob['description']>;
  type: FormControl<IImob['type']>;
  categ: FormControl<IImob['categ']>;
  service: FormControl<IImob['service']>;
  price: FormControl<IImob['price']>;
  priceCurrency: FormControl<IImob['priceCurrency']>;
  tags: FormControl<IImob['tags']>;
  address: FormControl<IImob['address']>;
  contact: FormControl<IImob['contact']>;
  nbofRooms: FormControl<IImob['nbofRooms']>;
  constrYear: FormControl<IImob['constrYear']>;
  useSurface: FormControl<IImob['useSurface']>;
  builtSurface: FormControl<IImob['builtSurface']>;
  compart: FormControl<IImob['compart']>;
  confort: FormControl<IImob['confort']>;
  floor: FormControl<IImob['floor']>;
  nbofKitchens: FormControl<IImob['nbofKitchens']>;
  nbofBthrooms: FormControl<IImob['nbofBthrooms']>;
  unitType: FormControl<IImob['unitType']>;
  unitHeight: FormControl<IImob['unitHeight']>;
  nbofBalconies: FormControl<IImob['nbofBalconies']>;
  utilities: FormControl<IImob['utilities']>;
  features: FormControl<IImob['features']>;
  otherdetails: FormControl<IImob['otherdetails']>;
  zoneDetails: FormControl<IImob['zoneDetails']>;
  availability: FormControl<IImob['availability']>;
  // ownerid: FormControl<IImob['ownerid']>;
  imobstouser: FormControl<IImob['imobstouser']>;
};

export type ImobFormGroup = FormGroup<ImobFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ImobFormService {
  createImobFormGroup(imob: ImobFormGroupInput = { id: null }): ImobFormGroup {
    const imobRawValue = {
      ...this.getFormDefaults(),
      ...imob,
    };
    return new FormGroup<ImobFormGroupContent>({
      id: new FormControl(
        { value: imobRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(imobRawValue.title, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(30)],
      }),
      description: new FormControl(imobRawValue.description, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(1000)],
      }),
      type: new FormControl(imobRawValue.type, {
        validators: [Validators.required],
      }),
      categ: new FormControl(imobRawValue.categ, {
        validators: [Validators.required],
      }),
      service: new FormControl(imobRawValue.service, {
        validators: [Validators.required],
      }),
      price: new FormControl(imobRawValue.price, {
        validators: [Validators.required, Validators.min(1000.0)],
      }),
      priceCurrency: new FormControl(imobRawValue.priceCurrency, {
        validators: [Validators.required],
      }),
      tags: new FormControl(imobRawValue.tags),
      address: new FormControl(imobRawValue.address, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(50)],
      }),
      contact: new FormControl(imobRawValue.contact, {
        validators: [Validators.required, Validators.minLength(10)],
      }),
      nbofRooms: new FormControl(imobRawValue.nbofRooms, {
        validators: [Validators.required, Validators.min(1)],
      }),
      constrYear: new FormControl(imobRawValue.constrYear, {
        validators: [Validators.required],
      }),
      useSurface: new FormControl(imobRawValue.useSurface, {
        validators: [Validators.required],
      }),
      builtSurface: new FormControl(imobRawValue.builtSurface, {
        validators: [Validators.required],
      }),
      compart: new FormControl(imobRawValue.compart),
      confort: new FormControl(imobRawValue.confort, {
        validators: [Validators.required],
      }),
      floor: new FormControl(imobRawValue.floor),
      nbofKitchens: new FormControl(imobRawValue.nbofKitchens, {
        validators: [Validators.required, Validators.min(1), Validators.max(10)],
      }),
      nbofBthrooms: new FormControl(imobRawValue.nbofBthrooms, {
        validators: [Validators.required, Validators.minLength(1), Validators.maxLength(10)],
      }),
      unitType: new FormControl(imobRawValue.unitType),
      unitHeight: new FormControl(imobRawValue.unitHeight),
      nbofBalconies: new FormControl(imobRawValue.nbofBalconies),
      utilities: new FormControl(imobRawValue.utilities, {
        validators: [Validators.minLength(3), Validators.maxLength(50)],
      }),
      features: new FormControl(imobRawValue.features, {
        validators: [Validators.minLength(3), Validators.maxLength(50)],
      }),
      otherdetails: new FormControl(imobRawValue.otherdetails, {
        validators: [Validators.minLength(3), Validators.maxLength(50)],
      }),
      zoneDetails: new FormControl(imobRawValue.zoneDetails, {
        validators: [Validators.minLength(3), Validators.maxLength(50)],
      }),
      availability: new FormControl(imobRawValue.availability, {
        validators: [Validators.minLength(3), Validators.maxLength(10)],
      }),
      // ownerid: new FormControl(imobRawValue.ownerid),
      imobstouser: new FormControl(imobRawValue.imobstouser),
    });
  }

  getImob(form: ImobFormGroup): IImob | NewImob {
    return form.getRawValue() as IImob | NewImob;
  }

  resetForm(form: ImobFormGroup, imob: ImobFormGroupInput): void {
    const imobRawValue = { ...this.getFormDefaults(), ...imob };
    form.reset(
      {
        ...imobRawValue,
        id: { value: imobRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ImobFormDefaults {
    return {
      id: null,
    };
  }
}
