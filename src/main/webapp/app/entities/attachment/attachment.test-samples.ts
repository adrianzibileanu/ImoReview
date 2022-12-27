import { IAttachment, NewAttachment } from './attachment.model';

export const sampleWithRequiredData: IAttachment = {
  id: '67dea94e-98c1-4e73-a17b-e4abf6e7744a',
  name: 'Sausages Object-based',
  cvFile: '../fake-data/blob/hipster.png',
  cvFileContentType: 'unknown',
  cvFileContentType: 'interfaces Reunion haptic',
};

export const sampleWithPartialData: IAttachment = {
  id: 'f45e93da-0dcd-44c0-8555-7041e349133b',
  name: 'RSS bluetooth purple',
  cvFile: '../fake-data/blob/hipster.png',
  cvFileContentType: 'unknown',
  cvFileContentType: 'SSL whiteboard',
};

export const sampleWithFullData: IAttachment = {
  id: '8cb3ee72-e718-44a8-b023-eed9c0460693',
  name: 'vertical payment',
  cvFile: '../fake-data/blob/hipster.png',
  cvFileContentType: 'unknown',
  cvFileContentType: 'Mehedinti Hunedoara',
};

export const sampleWithNewData: NewAttachment = {
  name: 'Fantastic Fresh',
  cvFile: '../fake-data/blob/hipster.png',
  cvFileContentType: 'unknown',
  cvFileContentType: 'Engineer',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
