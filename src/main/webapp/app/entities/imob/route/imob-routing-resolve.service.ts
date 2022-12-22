import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IImob } from '../imob.model';
import { ImobService } from '../service/imob.service';

@Injectable({ providedIn: 'root' })
export class ImobRoutingResolveService implements Resolve<IImob | null> {
  constructor(protected service: ImobService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IImob | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((imob: HttpResponse<IImob>) => {
          if (imob.body) {
            return of(imob.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
