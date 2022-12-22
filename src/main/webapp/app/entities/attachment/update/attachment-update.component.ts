import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AttachmentFormService, AttachmentFormGroup } from './attachment-form.service';
import { IAttachment } from '../attachment.model';
import { AttachmentService } from '../service/attachment.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-attachment-update',
  templateUrl: './attachment-update.component.html',
})
export class AttachmentUpdateComponent implements OnInit {
  isSaving = false;
  attachment: IAttachment | null = null;

  usersSharedCollection: IUser[] = [];

  editForm: AttachmentFormGroup = this.attachmentFormService.createAttachmentFormGroup();

  constructor(
    protected attachmentService: AttachmentService,
    protected attachmentFormService: AttachmentFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attachment }) => {
      this.attachment = attachment;
      if (attachment) {
        this.updateForm(attachment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const attachment = this.attachmentFormService.getAttachment(this.editForm);
    if (attachment.id !== null) {
      this.subscribeToSaveResponse(this.attachmentService.update(attachment));
    } else {
      this.subscribeToSaveResponse(this.attachmentService.create(attachment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttachment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(attachment: IAttachment): void {
    this.attachment = attachment;
    this.attachmentFormService.resetForm(this.editForm, attachment);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      ...(attachment.manytomanies ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, ...(this.attachment?.manytomanies ?? []))))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
