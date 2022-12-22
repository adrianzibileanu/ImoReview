import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { SubType } from 'app/entities/enumerations/sub-type.model';

export interface ISub {
  id: string;
  subscribed?: boolean | null;
  active?: boolean | null;
  expirationDate?: dayjs.Dayjs | null;
  type?: SubType | null;
  userID?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewSub = Omit<ISub, 'id'> & { id: null };
