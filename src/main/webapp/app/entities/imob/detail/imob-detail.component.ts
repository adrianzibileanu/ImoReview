import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IImob } from '../imob.model';

@Component({
  selector: 'jhi-imob-detail',
  templateUrl: './imob-detail.component.html',
})
export class ImobDetailComponent implements OnInit {
  imob: IImob | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ imob }) => {
      this.imob = imob;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
