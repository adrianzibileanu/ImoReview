import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ImobFormService } from './imob-form.service';
import { ImobService } from '../service/imob.service';
import { IImob } from '../imob.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { ImobUpdateComponent } from './imob-update.component';

describe('Imob Management Update Component', () => {
  let comp: ImobUpdateComponent;
  let fixture: ComponentFixture<ImobUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let imobFormService: ImobFormService;
  let imobService: ImobService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ImobUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ImobUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ImobUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    imobFormService = TestBed.inject(ImobFormService);
    imobService = TestBed.inject(ImobService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const imob: IImob = { id: 'CBA' };
      const imobstouser: IUser = { id: 'f4cc0668-dedc-46d8-b04a-bcbe981e7b65' };
      imob.imobstouser = imobstouser;

      const userCollection: IUser[] = [{ id: '4dd30e02-c173-46b1-978c-6d797089b414' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [imobstouser];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ imob });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const imob: IImob = { id: 'CBA' };
      const imobstouser: IUser = { id: '8f863d11-14ad-4082-bcdc-1eda35754ced' };
      imob.imobstouser = imobstouser;

      activatedRoute.data = of({ imob });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(imobstouser);
      expect(comp.imob).toEqual(imob);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImob>>();
      const imob = { id: 'ABC' };
      jest.spyOn(imobFormService, 'getImob').mockReturnValue(imob);
      jest.spyOn(imobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imob }));
      saveSubject.complete();

      // THEN
      expect(imobFormService.getImob).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(imobService.update).toHaveBeenCalledWith(expect.objectContaining(imob));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImob>>();
      const imob = { id: 'ABC' };
      jest.spyOn(imobFormService, 'getImob').mockReturnValue({ id: null });
      jest.spyOn(imobService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imob: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imob }));
      saveSubject.complete();

      // THEN
      expect(imobFormService.getImob).toHaveBeenCalled();
      expect(imobService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImob>>();
      const imob = { id: 'ABC' };
      jest.spyOn(imobService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imob });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(imobService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
