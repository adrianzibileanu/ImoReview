import dayjs from 'dayjs/esm';

import { ImobType } from 'app/entities/enumerations/imob-type.model';
import { ImobCateg } from 'app/entities/enumerations/imob-categ.model';
import { ImobServ } from 'app/entities/enumerations/imob-serv.model';
import { Currency } from 'app/entities/enumerations/currency.model';

import { IImob, NewImob } from './imob.model';

export const sampleWithRequiredData: IImob = {
  id: '72d3d234-b7d2-4183-b905-ecbd3be1c84c',
  title: 'microchip Supervisor',
  description: 'repurpose Intrarea',
  type: ImobType['HSE'],
  categ: ImobCateg['RZD'],
  service: ImobServ['RENT'],
  price: 23000,
  priceCurrency: Currency['EUR'],
  address: 'Incredible',
  contact: undefined,
  nbofRooms: 15220,
  constrYear: dayjs('2022-12-20'),
  useSurface: 65609,
  builtSurface: 'methodologies Giurgiu rich',
  confort: 'application Fresh',
  nbofKitchens: 5,
  nbofBthrooms: 'Electronic',
};

export const sampleWithPartialData: IImob = {
  id: '4155cc5e-6121-4445-8c5a-9978b4714fdf',
  title: 'calculate Tools',
  description: 'Bulevardul',
  type: ImobType['PNT'],
  categ: ImobCateg['COMM'],
  service: ImobServ['RENT'],
  price: 76127,
  priceCurrency: Currency['GBP'],
  tags: 'ROI',
  address: 'Seychelles compressing withdrawal',
  contact: undefined,
  nbofRooms: 13282,
  constrYear: dayjs('2022-12-20'),
  useSurface: 69712,
  builtSurface: 'Intrarea',
  confort: 'programming Metal',
  floor: 86679,
  nbofKitchens: 10,
  nbofBthrooms: 'Intrarea 1',
  unitHeight: 'wireless',
  nbofBalconies: 'De-engineered Bulevardul',
  features: 'azure back',
  availability: 'Teleorman ',
};

export const sampleWithFullData: IImob = {
  id: '4b1fa0d1-2079-4bfe-ac81-84ec82df1be5',
  title: 'firewall Frozen structure',
  description: 'Soft',
  type: ImobType['BRO'],
  categ: ImobCateg['RZD'],
  service: ImobServ['RENT'],
  price: 84472,
  priceCurrency: Currency['EUR'],
  tags: 'Global Business-focused',
  address: 'panel Granite Congo',
  contact: undefined,
  nbofRooms: 26396,
  constrYear: dayjs('2022-12-20'),
  useSurface: 99551,
  builtSurface: 'technologies Timis',
  compart: 'Steel Vaslui',
  confort: 'intermediate Tasty',
  floor: 31365,
  nbofKitchens: 5,
  nbofBthrooms: 'Quality pl',
  unitType: 'Auto Savings',
  unitHeight: 'Brasov Architect',
  nbofBalconies: 'Steel',
  utilities: 'Fresh Reactive methodologies',
  features: 'Arges',
  otherdetails: 'Ergonomic Tactics protocol',
  zoneDetails: 'B2C',
  availability: 'Lebanon',
  ownerid: 'TCP Savings Aleea',
};

export const sampleWithNewData: NewImob = {
  title: 'Tuna',
  description: 'Communications',
  type: ImobType['HTP'],
  categ: ImobCateg['COMM'],
  service: ImobServ['RENT'],
  price: 74659,
  priceCurrency: Currency['GBP'],
  address: 'invoice Guam',
  contact: undefined,
  nbofRooms: 17196,
  constrYear: dayjs('2022-12-20'),
  useSurface: 17279,
  builtSurface: 'Coordinator Savings',
  confort: 'Argentina',
  nbofKitchens: 9,
  nbofBthrooms: 'Infrastruc',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
