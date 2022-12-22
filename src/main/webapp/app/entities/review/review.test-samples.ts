import { IReview, NewReview } from './review.model';

export const sampleWithRequiredData: IReview = {
  id: '589fcc68-2ddc-4ceb-934b-2ca914347f04',
  title: 'Nakfa migration SAS',
  body: 'RSS SQL services',
  rating: 57922,
};

export const sampleWithPartialData: IReview = {
  id: '3cbc862f-5c02-4df6-9609-322f06a2d8be',
  title: 'blue',
  body: 'Saint Frozen',
  rating: 68440,
};

export const sampleWithFullData: IReview = {
  id: 'b153d9a8-4cdc-4914-bb73-8bdcdce329d1',
  title: 'synthesizing bus Zlo',
  body: 'open-source synthesize wireless',
  rating: 25554,
  isImob: true,
};

export const sampleWithNewData: NewReview = {
  title: 'enterprise Alba Acco',
  body: 'Checking killer',
  rating: 92443,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
