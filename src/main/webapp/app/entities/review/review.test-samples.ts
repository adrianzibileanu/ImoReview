import { IReview, NewReview } from './review.model';

export const sampleWithRequiredData: IReview = {
  id: '589fcc68-2ddc-4ceb-934b-2ca914347f04',
  title: 'Nakfa migration SAS',
  body: 'RSS SQL services',
  rating: 57922,
};

export const sampleWithPartialData: IReview = {
  id: '43cbc862-f5c0-42df-a960-9322f06a2d8b',
  title: 'mint Fantastic Froze',
  body: 'bandwidth fuchsia payment',
  rating: 79520,
};

export const sampleWithFullData: IReview = {
  id: 'dc9143b7-38bd-4cdc-a329-d1ddcbbd6ba4',
  title: 'open-source synthesi',
  body: 'matrix',
  rating: 76328,
};

export const sampleWithNewData: NewReview = {
  title: 'Alba',
  body: 'USB Checking',
  rating: 35734,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
