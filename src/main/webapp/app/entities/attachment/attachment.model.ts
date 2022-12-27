import { IUser } from 'app/entities/user/user.model';

export interface IAttachment {
  id: string;
  name?: string | null;
  cvFile?: string | null;
  cvFileContentType?: string | null;

  manytoone?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewAttachment = Omit<IAttachment, 'id'> & { id: null };
