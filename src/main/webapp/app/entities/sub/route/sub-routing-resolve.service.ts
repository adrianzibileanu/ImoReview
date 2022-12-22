import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISub } from '../sub.model';
import { SubService } from '../service/sub.service';

@Injectable({ providedIn: 'root' })
export class SubRoutingResolveService implements Resolve<ISub | null> {
  constructor(protected service: SubService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISub | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sub: HttpResponse<ISub>) => {
          if (sub.body) {
            return of(sub.body);
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
