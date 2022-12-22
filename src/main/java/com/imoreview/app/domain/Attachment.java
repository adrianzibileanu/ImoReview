package com.imoreview.app.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Attachment.
 */
@Document(collection = "attachment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Field("file_name")
    private String fileName;

    @NotNull(message = "must not be null")
    @Field("original_file_name")
    private String originalFileName;

    @NotNull(message = "must not be null")
    @Field("extension")
    private String extension;

    @NotNull(message = "must not be null")
    @Field("size_in_bytes")
    private Integer sizeInBytes;

    @NotNull(message = "must not be null")
    @Field("uploaded_date")
    private Instant uploadedDate;

    @NotNull(message = "must not be null")
    @Field("sha_256")
    private String sha256;

    @NotNull(message = "must not be null")
    @Field("content_type")
    private String contentType;

    @Field("manytomanies")
    private Set<User> manytomanies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Attachment id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Attachment fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFileName() {
        return this.originalFileName;
    }

    public Attachment originalFileName(String originalFileName) {
        this.setOriginalFileName(originalFileName);
        return this;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getExtension() {
        return this.extension;
    }

    public Attachment extension(String extension) {
        this.setExtension(extension);
        return this;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Integer getSizeInBytes() {
        return this.sizeInBytes;
    }

    public Attachment sizeInBytes(Integer sizeInBytes) {
        this.setSizeInBytes(sizeInBytes);
        return this;
    }

    public void setSizeInBytes(Integer sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public Instant getUploadedDate() {
        return this.uploadedDate;
    }

    public Attachment uploadedDate(Instant uploadedDate) {
        this.setUploadedDate(uploadedDate);
        return this;
    }

    public void setUploadedDate(Instant uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public String getSha256() {
        return this.sha256;
    }

    public Attachment sha256(String sha256) {
        this.setSha256(sha256);
        return this;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getContentType() {
        return this.contentType;
    }

    public Attachment contentType(String contentType) {
        this.setContentType(contentType);
        return this;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Set<User> getManytomanies() {
        return this.manytomanies;
    }

    public void setManytomanies(Set<User> users) {
        this.manytomanies = users;
    }

    public Attachment manytomanies(Set<User> users) {
        this.setManytomanies(users);
        return this;
    }

    public Attachment addManytomany(User user) {
        this.manytomanies.add(user);
        return this;
    }

    public Attachment removeManytomany(User user) {
        this.manytomanies.remove(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attachment)) {
            return false;
        }
        return id != null && id.equals(((Attachment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Attachment{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", originalFileName='" + getOriginalFileName() + "'" +
            ", extension='" + getExtension() + "'" +
            ", sizeInBytes=" + getSizeInBytes() +
            ", uploadedDate='" + getUploadedDate() + "'" +
            ", sha256='" + getSha256() + "'" +
            ", contentType='" + getContentType() + "'" +
            "}";
    }
}
