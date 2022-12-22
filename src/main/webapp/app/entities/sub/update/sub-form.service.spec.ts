import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../sub.test-samples';

import { SubFormService } from './sub-form.service';

describe('Sub Form Service', () => {
  let service: SubFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SubFormService);
  });

  describe('Service methods', () => {
    describe('createSubFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSubFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            subscribed: expect.any(Object),
            active: expect.any(Object),
            expirationDate: expect.any(Object),
            type: expect.any(Object),
            userID: expect.any(Object),
          })
        );
      });

      it('passing ISub should create a new form with FormGroup', () => {
        const formGroup = service.createSubFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            subscribed: expect.any(Object),
            active: expect.any(Object),
            expirationDate: expect.any(Object),
            type: expect.any(Object),
            userID: expect.any(Object),
          })
        );
      });
    });

    describe('getSub', () => {
      it('should return NewSub for default Sub initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSubFormGroup(sampleWithNewData);

        const sub = service.getSub(formGroup) as any;

        expect(sub).toMatchObject(sampleWithNewData);
      });

      it('should return NewSub for empty Sub initial value', () => {
        const formGroup = service.createSubFormGroup();

        const sub = service.getSub(formGroup) as any;

        expect(sub).toMatchObject({});
      });

      it('should return ISub', () => {
        const formGroup = service.createSubFormGroup(sampleWithRequiredData);

        const sub = service.getSub(formGroup) as any;

        expect(sub).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISub should not enable id FormControl', () => {
        const formGroup = service.createSubFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSub should disable id FormControl', () => {
        const formGroup = service.createSubFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
