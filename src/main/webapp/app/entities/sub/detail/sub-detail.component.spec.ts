import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SubDetailComponent } from './sub-detail.component';

describe('Sub Management Detail Component', () => {
  let comp: SubDetailComponent;
  let fixture: ComponentFixture<SubDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SubDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sub: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(SubDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SubDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sub on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sub).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
