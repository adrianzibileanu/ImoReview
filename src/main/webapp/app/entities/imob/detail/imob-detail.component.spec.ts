import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ImobDetailComponent } from './imob-detail.component';

describe('Imob Management Detail Component', () => {
  let comp: ImobDetailComponent;
  let fixture: ComponentFixture<ImobDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ImobDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ imob: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(ImobDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ImobDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load imob on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.imob).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
