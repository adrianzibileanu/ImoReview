package com.imoreview.app.domain;

import java.io.Serializable;
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
    @Field("name")
    private String name;

    @Field("cv_file")
    private byte[] cvFile;

    @NotNull(message = "must not be null")
    @NotNull
    @Field("cv_file_content_type")
    private String cvFileContentType;

    @Field("manytoone")
    private User manytoone;

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

    public String getName() {
        return this.name;
    }

    public Attachment name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getCvFile() {
        return this.cvFile;
    }

    public Attachment cvFile(byte[] cvFile) {
        this.setCvFile(cvFile);
        return this;
    }

    public void setCvFile(byte[] cvFile) {
        this.cvFile = cvFile;
    }

    public String getCvFileContentType() {
        return this.cvFileContentType;
    }

    public Attachment cvFileContentType(String cvFileContentType) {
        this.setCvFileContentType(cvFileContentType);
        return this;
    }

    public void setCvFileContentType(String cvFileContentType) {
        this.cvFileContentType = cvFileContentType;
    }

    public User getManytoone() {
        return this.manytoone;
    }

    public void setManytoone(User user) {
        this.manytoone = user;
    }

    public Attachment manytoone(User user) {
        this.setManytoone(user);
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
            ", name='" + getName() + "'" +
            ", cvFile='" + getCvFile() + "'" +
            ", cvFileContentType='" + getCvFileContentType() + "'" +
            ", cvFileContentType='" + getCvFileContentType() + "'" +
            "}";
    }
}
