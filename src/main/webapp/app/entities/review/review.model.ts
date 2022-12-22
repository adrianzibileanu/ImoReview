import { IImob } from 'app/entities/imob/imob.model';
import { IUser } from 'app/entities/user/user.model';

export interface IReview {
  id: string;
  title?: string | null;
  body?: string | null;
  rating?: number | null;
  isImob?: boolean | null;
  imobID?: Pick<IImob, 'id'> | null;
  userID?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewReview = Omit<IReview, 'id'> & { id: null };
