package com.imoreview.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Review.
 */
@Document(collection = "review")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Size(min = 3, max = 20)
    @Field("title")
    private String title;

    @NotNull(message = "must not be null")
    @Size(min = 3, max = 800)
    @Field("body")
    private String body;

    @NotNull(message = "must not be null")
    @Field("rating")
    private Integer rating;

    @Field("is_imob")
    private Boolean isImob;

    @Field("imobID")
    @JsonIgnoreProperties(value = { "imobstouser" }, allowSetters = true)
    private Imob imobID;

    @Field("userID")
    private User userID;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Review id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Review title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return this.body;
    }

    public Review body(String body) {
        this.setBody(body);
        return this;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getRating() {
        return this.rating;
    }

    public Review rating(Integer rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Boolean getIsImob() {
        return this.isImob;
    }

    public Review isImob(Boolean isImob) {
        this.setIsImob(isImob);
        return this;
    }

    public void setIsImob(Boolean isImob) {
        this.isImob = isImob;
    }

    public Imob getImobID() {
        return this.imobID;
    }

    public void setImobID(Imob imob) {
        this.imobID = imob;
    }

    public Review imobID(Imob imob) {
        this.setImobID(imob);
        return this;
    }

    public User getUserID() {
        return this.userID;
    }

    public void setUserID(User user) {
        this.userID = user;
    }

    public Review userID(User user) {
        this.setUserID(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Review)) {
            return false;
        }
        return id != null && id.equals(((Review) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Review{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", body='" + getBody() + "'" +
            ", rating=" + getRating() +
            ", isImob='" + getIsImob() + "'" +
            "}";
    }
}
