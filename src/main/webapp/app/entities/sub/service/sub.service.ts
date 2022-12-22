import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISub, NewSub } from '../sub.model';

export type PartialUpdateSub = Partial<ISub> & Pick<ISub, 'id'>;

type RestOf<T extends ISub | NewSub> = Omit<T, 'expirationDate'> & {
  expirationDate?: string | null;
};

export type RestSub = RestOf<ISub>;

export type NewRestSub = RestOf<NewSub>;

export type PartialUpdateRestSub = RestOf<PartialUpdateSub>;

export type EntityResponseType = HttpResponse<ISub>;
export type EntityArrayResponseType = HttpResponse<ISub[]>;

@Injectable({ providedIn: 'root' })
export class SubService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/subs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sub: NewSub): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sub);
    return this.http.post<RestSub>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(sub: ISub): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sub);
    return this.http
      .put<RestSub>(`${this.resourceUrl}/${this.getSubIdentifier(sub)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(sub: PartialUpdateSub): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sub);
    return this.http
      .patch<RestSub>(`${this.resourceUrl}/${this.getSubIdentifier(sub)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestSub>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSub[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSubIdentifier(sub: Pick<ISub, 'id'>): string {
    return sub.id;
  }

  compareSub(o1: Pick<ISub, 'id'> | null, o2: Pick<ISub, 'id'> | null): boolean {
    return o1 && o2 ? this.getSubIdentifier(o1) === this.getSubIdentifier(o2) : o1 === o2;
  }

  addSubToCollectionIfMissing<Type extends Pick<ISub, 'id'>>(subCollection: Type[], ...subsToCheck: (Type | null | undefined)[]): Type[] {
    const subs: Type[] = subsToCheck.filter(isPresent);
    if (subs.length > 0) {
      const subCollectionIdentifiers = subCollection.map(subItem => this.getSubIdentifier(subItem)!);
      const subsToAdd = subs.filter(subItem => {
        const subIdentifier = this.getSubIdentifier(subItem);
        if (subCollectionIdentifiers.includes(subIdentifier)) {
          return false;
        }
        subCollectionIdentifiers.push(subIdentifier);
        return true;
      });
      return [...subsToAdd, ...subCollection];
    }
    return subCollection;
  }

  protected convertDateFromClient<T extends ISub | NewSub | PartialUpdateSub>(sub: T): RestOf<T> {
    return {
      ...sub,
      expirationDate: sub.expirationDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restSub: RestSub): ISub {
    return {
      ...restSub,
      expirationDate: restSub.expirationDate ? dayjs(restSub.expirationDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSub>): HttpResponse<ISub> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSub[]>): HttpResponse<ISub[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
