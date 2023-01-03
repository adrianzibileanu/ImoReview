import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ReviewFormService } from './review-form.service';
import { ReviewService } from '../service/review.service';
import { IReview } from '../review.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { ReviewUpdateComponent } from './review-update.component';

describe('Review Management Update Component', () => {
  let comp: ReviewUpdateComponent;
  let fixture: ComponentFixture<ReviewUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reviewFormService: ReviewFormService;
  let reviewService: ReviewService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ReviewUpdateComponent],
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
      .overrideTemplate(ReviewUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReviewUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reviewFormService = TestBed.inject(ReviewFormService);
    reviewService = TestBed.inject(ReviewService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const review: IReview = { id: 'CBA' };
      const userID: IUser = { id: '755a9b0b-19a0-44da-866a-57252d2da965' };
      review.userID = userID;

      const userCollection: IUser[] = [{ id: '129700f7-2c09-4662-99cb-f33e3aa287c9' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [userID];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ review });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const review: IReview = { id: 'CBA' };
      const userID: IUser = { id: '7bd9399d-429c-4239-bf9d-3fce67c71d31' };
      review.userID = userID;

      activatedRoute.data = of({ review });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(userID);
      expect(comp.review).toEqual(review);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReview>>();
      const review = { id: 'ABC' };
      jest.spyOn(reviewFormService, 'getReview').mockReturnValue(review);
      jest.spyOn(reviewService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ review });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: review }));
      saveSubject.complete();

      // THEN
      expect(reviewFormService.getReview).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reviewService.update).toHaveBeenCalledWith(expect.objectContaining(review));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReview>>();
      const review = { id: 'ABC' };
      jest.spyOn(reviewFormService, 'getReview').mockReturnValue({ id: null });
      jest.spyOn(reviewService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ review: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: review }));
      saveSubject.complete();

      // THEN
      expect(reviewFormService.getReview).toHaveBeenCalled();
      expect(reviewService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReview>>();
      const review = { id: 'ABC' };
      jest.spyOn(reviewService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ review });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reviewService.update).toHaveBeenCalled();
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
