import dayjs from 'dayjs/esm';

import { SubType } from 'app/entities/enumerations/sub-type.model';

import { ISub, NewSub } from './sub.model';

export const sampleWithRequiredData: ISub = {
  id: '3e7bd504-3c5c-4608-8a82-fbc566feb573',
};

export const sampleWithPartialData: ISub = {
  id: '4b797e2f-6673-4c16-ab8c-def593f9e27a',
  subscribed: true,
  active: true,
};

export const sampleWithFullData: ISub = {
  id: '176967da-339e-46bd-b092-e9c3a335d836',
  subscribed: true,
  active: true,
  expirationDate: dayjs('2022-12-21'),
  type: SubType['MTH'],
};

export const sampleWithNewData: NewSub = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
