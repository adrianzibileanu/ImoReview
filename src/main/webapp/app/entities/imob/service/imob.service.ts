import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IImob, NewImob } from '../imob.model';

export type PartialUpdateImob = Partial<IImob> & Pick<IImob, 'id'>;

type RestOf<T extends IImob | NewImob> = Omit<T, 'constrYear'> & {
  constrYear?: string | null;
};

export type RestImob = RestOf<IImob>;

export type NewRestImob = RestOf<NewImob>;

export type PartialUpdateRestImob = RestOf<PartialUpdateImob>;

export type EntityResponseType = HttpResponse<IImob>;
export type EntityArrayResponseType = HttpResponse<IImob[]>;

@Injectable({ providedIn: 'root' })
export class ImobService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/imobs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(imob: NewImob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imob);
    return this.http.post<RestImob>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(imob: IImob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imob);
    return this.http
      .put<RestImob>(`${this.resourceUrl}/${this.getImobIdentifier(imob)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(imob: PartialUpdateImob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(imob);
    return this.http
      .patch<RestImob>(`${this.resourceUrl}/${this.getImobIdentifier(imob)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestImob>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestImob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getImobIdentifier(imob: Pick<IImob, 'id'>): string {
    return imob.id;
  }

  compareImob(o1: Pick<IImob, 'id'> | null, o2: Pick<IImob, 'id'> | null): boolean {
    return o1 && o2 ? this.getImobIdentifier(o1) === this.getImobIdentifier(o2) : o1 === o2;
  }

  addImobToCollectionIfMissing<Type extends Pick<IImob, 'id'>>(
    imobCollection: Type[],
    ...imobsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const imobs: Type[] = imobsToCheck.filter(isPresent);
    if (imobs.length > 0) {
      const imobCollectionIdentifiers = imobCollection.map(imobItem => this.getImobIdentifier(imobItem)!);
      const imobsToAdd = imobs.filter(imobItem => {
        const imobIdentifier = this.getImobIdentifier(imobItem);
        if (imobCollectionIdentifiers.includes(imobIdentifier)) {
          return false;
        }
        imobCollectionIdentifiers.push(imobIdentifier);
        return true;
      });
      return [...imobsToAdd, ...imobCollection];
    }
    return imobCollection;
  }

  protected convertDateFromClient<T extends IImob | NewImob | PartialUpdateImob>(imob: T): RestOf<T> {
    return {
      ...imob,
      constrYear: imob.constrYear?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restImob: RestImob): IImob {
    return {
      ...restImob,
      constrYear: restImob.constrYear ? dayjs(restImob.constrYear) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestImob>): HttpResponse<IImob> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestImob[]>): HttpResponse<IImob[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
