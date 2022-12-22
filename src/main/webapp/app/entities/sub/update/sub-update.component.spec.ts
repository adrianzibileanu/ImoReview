import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SubFormService } from './sub-form.service';
import { SubService } from '../service/sub.service';
import { ISub } from '../sub.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { SubUpdateComponent } from './sub-update.component';

describe('Sub Management Update Component', () => {
  let comp: SubUpdateComponent;
  let fixture: ComponentFixture<SubUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let subFormService: SubFormService;
  let subService: SubService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SubUpdateComponent],
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
      .overrideTemplate(SubUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SubUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    subFormService = TestBed.inject(SubFormService);
    subService = TestBed.inject(SubService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const sub: ISub = { id: 'CBA' };
      const userID: IUser = { id: 'f758da8f-5e7f-47e4-a49a-8374c7789793' };
      sub.userID = userID;

      const userCollection: IUser[] = [{ id: 'c66d0eee-a519-493f-bb50-b5e73b37e367' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [userID];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sub });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sub: ISub = { id: 'CBA' };
      const userID: IUser = { id: '070df661-c0c3-4276-a4e0-fb46c5730cc4' };
      sub.userID = userID;

      activatedRoute.data = of({ sub });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(userID);
      expect(comp.sub).toEqual(sub);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISub>>();
      const sub = { id: 'ABC' };
      jest.spyOn(subFormService, 'getSub').mockReturnValue(sub);
      jest.spyOn(subService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sub });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sub }));
      saveSubject.complete();

      // THEN
      expect(subFormService.getSub).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(subService.update).toHaveBeenCalledWith(expect.objectContaining(sub));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISub>>();
      const sub = { id: 'ABC' };
      jest.spyOn(subFormService, 'getSub').mockReturnValue({ id: null });
      jest.spyOn(subService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sub: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sub }));
      saveSubject.complete();

      // THEN
      expect(subFormService.getSub).toHaveBeenCalled();
      expect(subService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISub>>();
      const sub = { id: 'ABC' };
      jest.spyOn(subService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sub });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(subService.update).toHaveBeenCalled();
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
