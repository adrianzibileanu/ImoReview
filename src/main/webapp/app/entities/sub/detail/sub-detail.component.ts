import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISub } from '../sub.model';

@Component({
  selector: 'jhi-sub-detail',
  templateUrl: './sub-detail.component.html',
})
export class SubDetailComponent implements OnInit {
  sub: ISub | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sub }) => {
      this.sub = sub;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
