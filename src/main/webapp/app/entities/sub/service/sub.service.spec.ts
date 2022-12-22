import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISub } from '../sub.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../sub.test-samples';

import { SubService, RestSub } from './sub.service';

const requireRestSample: RestSub = {
  ...sampleWithRequiredData,
  expirationDate: sampleWithRequiredData.expirationDate?.format(DATE_FORMAT),
};

describe('Sub Service', () => {
  let service: SubService;
  let httpMock: HttpTestingController;
  let expectedResult: ISub | ISub[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SubService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Sub', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const sub = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sub).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Sub', () => {
      const sub = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sub).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Sub', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Sub', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Sub', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSubToCollectionIfMissing', () => {
      it('should add a Sub to an empty array', () => {
        const sub: ISub = sampleWithRequiredData;
        expectedResult = service.addSubToCollectionIfMissing([], sub);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sub);
      });

      it('should not add a Sub to an array that contains it', () => {
        const sub: ISub = sampleWithRequiredData;
        const subCollection: ISub[] = [
          {
            ...sub,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSubToCollectionIfMissing(subCollection, sub);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Sub to an array that doesn't contain it", () => {
        const sub: ISub = sampleWithRequiredData;
        const subCollection: ISub[] = [sampleWithPartialData];
        expectedResult = service.addSubToCollectionIfMissing(subCollection, sub);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sub);
      });

      it('should add only unique Sub to an array', () => {
        const subArray: ISub[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const subCollection: ISub[] = [sampleWithRequiredData];
        expectedResult = service.addSubToCollectionIfMissing(subCollection, ...subArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sub: ISub = sampleWithRequiredData;
        const sub2: ISub = sampleWithPartialData;
        expectedResult = service.addSubToCollectionIfMissing([], sub, sub2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sub);
        expect(expectedResult).toContain(sub2);
      });

      it('should accept null and undefined values', () => {
        const sub: ISub = sampleWithRequiredData;
        expectedResult = service.addSubToCollectionIfMissing([], null, sub, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sub);
      });

      it('should return initial array if no Sub is added', () => {
        const subCollection: ISub[] = [sampleWithRequiredData];
        expectedResult = service.addSubToCollectionIfMissing(subCollection, undefined, null);
        expect(expectedResult).toEqual(subCollection);
      });
    });

    describe('compareSub', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSub(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareSub(entity1, entity2);
        const compareResult2 = service.compareSub(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareSub(entity1, entity2);
        const compareResult2 = service.compareSub(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareSub(entity1, entity2);
        const compareResult2 = service.compareSub(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
