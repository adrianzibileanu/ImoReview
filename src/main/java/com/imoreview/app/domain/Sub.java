package com.imoreview.app.domain;

import com.imoreview.app.domain.enumeration.SubType;
import java.io.Serializable;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Sub.
 */
@Document(collection = "sub")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Sub implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("subscribed")
    private Boolean subscribed;

    @Field("active")
    private Boolean active;

    @Field("expiration_date")
    private LocalDate expirationDate;

    @Field("type")
    private SubType type;

    @Field("userID")
    private User userID;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Sub id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getSubscribed() {
        return this.subscribed;
    }

    public Sub subscribed(Boolean subscribed) {
        this.setSubscribed(subscribed);
        return this;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Sub active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    public Sub expirationDate(LocalDate expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public SubType getType() {
        return this.type;
    }

    public Sub type(SubType type) {
        this.setType(type);
        return this;
    }

    public void setType(SubType type) {
        this.type = type;
    }

    public User getUserID() {
        return this.userID;
    }

    public void setUserID(User user) {
        this.userID = user;
    }

    public Sub userID(User user) {
        this.setUserID(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sub)) {
            return false;
        }
        return id != null && id.equals(((Sub) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sub{" +
            "id=" + getId() +
            ", subscribed='" + getSubscribed() + "'" +
            ", active='" + getActive() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
