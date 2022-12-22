package com.imoreview.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.imoreview.app.domain.enumeration.Currency;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Payment.
 */
@Document(collection = "payment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("price")
    private Double price;

    @NotNull(message = "must not be null")
    @Field("amount")
    private Double amount;

    @Field("currency")
    private Currency currency;

    @NotNull(message = "must not be null")
    @Field("terms")
    private Boolean terms;

    @NotNull(message = "must not be null")
    @Field("success")
    private Boolean success;

    @Field("subID")
    @JsonIgnoreProperties(value = { "userID" }, allowSetters = true)
    private Sub subID;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Payment id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getPrice() {
        return this.price;
    }

    public Payment price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Payment amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public Payment currency(Currency currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Boolean getTerms() {
        return this.terms;
    }

    public Payment terms(Boolean terms) {
        this.setTerms(terms);
        return this;
    }

    public void setTerms(Boolean terms) {
        this.terms = terms;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public Payment success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Sub getSubID() {
        return this.subID;
    }

    public void setSubID(Sub sub) {
        this.subID = sub;
    }

    public Payment subID(Sub sub) {
        this.setSubID(sub);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", amount=" + getAmount() +
            ", currency='" + getCurrency() + "'" +
            ", terms='" + getTerms() + "'" +
            ", success='" + getSuccess() + "'" +
            "}";
    }
}
