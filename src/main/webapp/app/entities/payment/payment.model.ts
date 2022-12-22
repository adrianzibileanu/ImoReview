import { ISub } from 'app/entities/sub/sub.model';
import { Currency } from 'app/entities/enumerations/currency.model';

export interface IPayment {
  id: string;
  price?: number | null;
  amount?: number | null;
  currency?: Currency | null;
  terms?: boolean | null;
  success?: boolean | null;
  subID?: Pick<ISub, 'id'> | null;
}

export type NewPayment = Omit<IPayment, 'id'> & { id: null };
