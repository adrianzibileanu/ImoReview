import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IAttachment {
  id: string;
  fileName?: string | null;
  originalFileName?: string | null;
  extension?: string | null;
  sizeInBytes?: number | null;
  uploadedDate?: dayjs.Dayjs | null;
  sha256?: string | null;
  contentType?: string | null;
  manytomanies?: Pick<IUser, 'id' | 'login'>[] | null;
}

export type NewAttachment = Omit<IAttachment, 'id'> & { id: null };
