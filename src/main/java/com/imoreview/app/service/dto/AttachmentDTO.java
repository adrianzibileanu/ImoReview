package com.imoreview.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.imoreview.app.domain.Attachment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttachmentDTO implements Serializable {

    private String id;

    @NotNull(message = "must not be null")
    private String name;

    private byte[] cvFile;

    @NotNull(message = "must not be null")
    private String cvFileContentType;

    private UserDTO manytoone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getCvFile() {
        return cvFile;
    }

    public void setCvFile(byte[] cvFile) {
        this.cvFile = cvFile;
    }

    public String getCvFileContentType() {
        return cvFileContentType;
    }

    public void setCvFileContentType(String cvFileContentType) {
        this.cvFileContentType = cvFileContentType;
    }

    public UserDTO getManytoone() {
        return manytoone;
    }

    public void setManytoone(UserDTO manytoone) {
        this.manytoone = manytoone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttachmentDTO)) {
            return false;
        }

        AttachmentDTO attachmentDTO = (AttachmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, attachmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttachmentDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", cvFile='" + getCvFile() + "'" +
            ", cvFileContentType='" + getCvFileContentType() + "'" +
            ", manytoone=" + getManytoone() +
            "}";
    }
}
