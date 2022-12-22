import dayjs from 'dayjs/esm';

import { IAttachment, NewAttachment } from './attachment.model';

export const sampleWithRequiredData: IAttachment = {
  id: '67dea94e-98c1-4e73-a17b-e4abf6e7744a',
  fileName: 'Sausages Object-based',
  originalFileName: 'interfaces Reunion haptic',
  extension: 'frame Account Peso',
  sizeInBytes: 80648,
  uploadedDate: dayjs('2022-12-21T22:36'),
  sha256: 'auxiliary',
  contentType: 'granular Computers',
};

export const sampleWithPartialData: IAttachment = {
  id: 'e349133b-bd7c-4ca9-b2b8-d396a48cb3ee',
  fileName: 'Sausages Plastic',
  originalFileName: 'Games SCSI',
  extension: 'auxiliary integrate',
  sizeInBytes: 61566,
  uploadedDate: dayjs('2022-12-22T13:31'),
  sha256: 'vertical payment',
  contentType: 'Mehedinti Hunedoara',
};

export const sampleWithFullData: IAttachment = {
  id: '91500bd3-53f3-4505-ae2e-d9ab7c953086',
  fileName: 'Implementation',
  originalFileName: 'Regional Intrarea Automotive',
  extension: 'Niger',
  sizeInBytes: 66118,
  uploadedDate: dayjs('2022-12-22T04:42'),
  sha256: 'attitude-oriented Bihor',
  contentType: 'Compatible',
};

export const sampleWithNewData: NewAttachment = {
  fileName: 'Caras-Severin exploit',
  originalFileName: 'port',
  extension: 'Andorra',
  sizeInBytes: 58654,
  uploadedDate: dayjs('2022-12-22T06:42'),
  sha256: 'Intrarea',
  contentType: 'Tulcea',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
