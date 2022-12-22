import { Currency } from 'app/entities/enumerations/currency.model';

import { IPayment, NewPayment } from './payment.model';

export const sampleWithRequiredData: IPayment = {
  id: '7bb75e76-458a-47ee-97c2-272eb2852f49',
  amount: 65143,
  terms: true,
  success: true,
};

export const sampleWithPartialData: IPayment = {
  id: '9e89be33-7efa-46a0-9709-ce2dc746aa30',
  amount: 99767,
  currency: Currency['GBP'],
  terms: false,
  success: false,
};

export const sampleWithFullData: IPayment = {
  id: '2c26408a-c410-40ba-8c37-e9ba3dbbb21e',
  price: 34238,
  amount: 5318,
  currency: Currency['GBP'],
  terms: true,
  success: false,
};

export const sampleWithNewData: NewPayment = {
  amount: 48506,
  terms: true,
  success: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
