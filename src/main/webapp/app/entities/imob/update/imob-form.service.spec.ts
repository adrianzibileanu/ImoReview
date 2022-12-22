import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../imob.test-samples';

import { ImobFormService } from './imob-form.service';

describe('Imob Form Service', () => {
  let service: ImobFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImobFormService);
  });

  describe('Service methods', () => {
    describe('createImobFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createImobFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            type: expect.any(Object),
            categ: expect.any(Object),
            service: expect.any(Object),
            price: expect.any(Object),
            priceCurrency: expect.any(Object),
            tags: expect.any(Object),
            address: expect.any(Object),
            contact: expect.any(Object),
            nbofRooms: expect.any(Object),
            constrYear: expect.any(Object),
            useSurface: expect.any(Object),
            builtSurface: expect.any(Object),
            compart: expect.any(Object),
            confort: expect.any(Object),
            floor: expect.any(Object),
            nbofKitchens: expect.any(Object),
            nbofBthrooms: expect.any(Object),
            unitType: expect.any(Object),
            unitHeight: expect.any(Object),
            nbofBalconies: expect.any(Object),
            utilities: expect.any(Object),
            features: expect.any(Object),
            otherdetails: expect.any(Object),
            zoneDetails: expect.any(Object),
            availability: expect.any(Object),
            ownerid: expect.any(Object),
            imobstouser: expect.any(Object),
          })
        );
      });

      it('passing IImob should create a new form with FormGroup', () => {
        const formGroup = service.createImobFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            type: expect.any(Object),
            categ: expect.any(Object),
            service: expect.any(Object),
            price: expect.any(Object),
            priceCurrency: expect.any(Object),
            tags: expect.any(Object),
            address: expect.any(Object),
            contact: expect.any(Object),
            nbofRooms: expect.any(Object),
            constrYear: expect.any(Object),
            useSurface: expect.any(Object),
            builtSurface: expect.any(Object),
            compart: expect.any(Object),
            confort: expect.any(Object),
            floor: expect.any(Object),
            nbofKitchens: expect.any(Object),
            nbofBthrooms: expect.any(Object),
            unitType: expect.any(Object),
            unitHeight: expect.any(Object),
            nbofBalconies: expect.any(Object),
            utilities: expect.any(Object),
            features: expect.any(Object),
            otherdetails: expect.any(Object),
            zoneDetails: expect.any(Object),
            availability: expect.any(Object),
            ownerid: expect.any(Object),
            imobstouser: expect.any(Object),
          })
        );
      });
    });

    describe('getImob', () => {
      it('should return NewImob for default Imob initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createImobFormGroup(sampleWithNewData);

        const imob = service.getImob(formGroup) as any;

        expect(imob).toMatchObject(sampleWithNewData);
      });

      it('should return NewImob for empty Imob initial value', () => {
        const formGroup = service.createImobFormGroup();

        const imob = service.getImob(formGroup) as any;

        expect(imob).toMatchObject({});
      });

      it('should return IImob', () => {
        const formGroup = service.createImobFormGroup(sampleWithRequiredData);

        const imob = service.getImob(formGroup) as any;

        expect(imob).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IImob should not enable id FormControl', () => {
        const formGroup = service.createImobFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewImob should disable id FormControl', () => {
        const formGroup = service.createImobFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
