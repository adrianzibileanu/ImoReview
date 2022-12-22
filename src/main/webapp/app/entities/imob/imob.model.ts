import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { ImobType } from 'app/entities/enumerations/imob-type.model';
import { ImobCateg } from 'app/entities/enumerations/imob-categ.model';
import { ImobServ } from 'app/entities/enumerations/imob-serv.model';
import { Currency } from 'app/entities/enumerations/currency.model';

export interface IImob {
  id: string;
  title?: string | null;
  description?: string | null;
  type?: ImobType | null;
  categ?: ImobCateg | null;
  service?: ImobServ | null;
  price?: number | null;
  priceCurrency?: Currency | null;
  tags?: string | null;
  address?: string | null;
  contact?: string | null;
  nbofRooms?: number | null;
  constrYear?: dayjs.Dayjs | null;
  useSurface?: number | null;
  builtSurface?: string | null;
  compart?: string | null;
  confort?: string | null;
  floor?: number | null;
  nbofKitchens?: number | null;
  nbofBthrooms?: string | null;
  unitType?: string | null;
  unitHeight?: string | null;
  nbofBalconies?: string | null;
  utilities?: string | null;
  features?: string | null;
  otherdetails?: string | null;
  zoneDetails?: string | null;
  availability?: string | null;
  // ownerid?: string | null;
  imobstouser?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewImob = Omit<IImob, 'id'> & { id: null };
