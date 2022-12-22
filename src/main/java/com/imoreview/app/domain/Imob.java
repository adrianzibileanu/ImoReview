package com.imoreview.app.domain;

import com.imoreview.app.domain.enumeration.Currency;
import com.imoreview.app.domain.enumeration.ImobCateg;
import com.imoreview.app.domain.enumeration.ImobServ;
import com.imoreview.app.domain.enumeration.ImobType;
import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Imob.
 */
@Document(collection = "imob")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Imob implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "must not be null")
    @Size(min = 3, max = 30)
    @Field("title")
    private String title;

    @NotNull(message = "must not be null")
    @Size(min = 3, max = 1000)
    @Field("description")
    private String description;

    @NotNull(message = "must not be null")
    @Field("type")
    private ImobType type;

    @NotNull(message = "must not be null")
    @Field("categ")
    private ImobCateg categ;

    @NotNull(message = "must not be null")
    @Field("service")
    private ImobServ service;

    @NotNull(message = "must not be null")
    @DecimalMin(value = "1000.00")
    @Field("price")
    private Double price;

    @NotNull(message = "must not be null")
    @Field("price_currency")
    private Currency priceCurrency;

    @Field("tags")
    private String tags;

    @NotNull(message = "must not be null")
    @Size(min = 3, max = 50)
    @Field("address")
    private String address;

    @NotNull(message = "must not be null")
    @Size(min = 10)
    @Field("contact")
    private String contact;

    @NotNull(message = "must not be null")
    @Min(value = 1)
    @Field("nbof_rooms")
    private Integer nbofRooms;

    @NotNull(message = "must not be null")
    @Field("constr_year")
    private LocalDate constrYear;

    @NotNull(message = "must not be null")
    @Field("use_surface")
    private Double useSurface;

    @NotNull(message = "must not be null")
    @Field("built_surface")
    private String builtSurface;

    @Field("compart")
    private String compart;

    @NotNull(message = "must not be null")
    @Field("confort")
    private String confort;

    @Field("floor")
    private Integer floor;

    @NotNull(message = "must not be null")
    @Min(value = 1)
    @Max(value = 10)
    @Field("nbof_kitchens")
    private Integer nbofKitchens;

    @NotNull(message = "must not be null")
    @Size(min = 1, max = 10)
    @Field("nbof_bthrooms")
    private String nbofBthrooms;

    @Field("unit_type")
    private String unitType;

    @Field("unit_height")
    private String unitHeight;

    @Field("nbof_balconies")
    private String nbofBalconies;

    @Size(min = 3, max = 50)
    @Field("utilities")
    private String utilities;

    @Size(min = 3, max = 50)
    @Field("features")
    private String features;

    @Size(min = 3, max = 50)
    @Field("otherdetails")
    private String otherdetails;

    @Size(min = 3, max = 50)
    @Field("zone_details")
    private String zoneDetails;

    @Size(min = 3, max = 10)
    @Field("availability")
    private String availability;

    /*   @Field("ownerid")
    private String ownerid;*/

    @Field("imobstouser")
    private User imobstouser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Imob id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Imob title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Imob description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImobType getType() {
        return this.type;
    }

    public Imob type(ImobType type) {
        this.setType(type);
        return this;
    }

    public void setType(ImobType type) {
        this.type = type;
    }

    public ImobCateg getCateg() {
        return this.categ;
    }

    public Imob categ(ImobCateg categ) {
        this.setCateg(categ);
        return this;
    }

    public void setCateg(ImobCateg categ) {
        this.categ = categ;
    }

    public ImobServ getService() {
        return this.service;
    }

    public Imob service(ImobServ service) {
        this.setService(service);
        return this;
    }

    public void setService(ImobServ service) {
        this.service = service;
    }

    public Double getPrice() {
        return this.price;
    }

    public Imob price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Currency getPriceCurrency() {
        return this.priceCurrency;
    }

    public Imob priceCurrency(Currency priceCurrency) {
        this.setPriceCurrency(priceCurrency);
        return this;
    }

    public void setPriceCurrency(Currency priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public String getTags() {
        return this.tags;
    }

    public Imob tags(String tags) {
        this.setTags(tags);
        return this;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getAddress() {
        return this.address;
    }

    public Imob address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return this.contact;
    }

    public Imob contact(String contact) {
        this.setContact(contact);
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Integer getNbofRooms() {
        return this.nbofRooms;
    }

    public Imob nbofRooms(Integer nbofRooms) {
        this.setNbofRooms(nbofRooms);
        return this;
    }

    public void setNbofRooms(Integer nbofRooms) {
        this.nbofRooms = nbofRooms;
    }

    public LocalDate getConstrYear() {
        return this.constrYear;
    }

    public Imob constrYear(LocalDate constrYear) {
        this.setConstrYear(constrYear);
        return this;
    }

    public void setConstrYear(LocalDate constrYear) {
        this.constrYear = constrYear;
    }

    public Double getUseSurface() {
        return this.useSurface;
    }

    public Imob useSurface(Double useSurface) {
        this.setUseSurface(useSurface);
        return this;
    }

    public void setUseSurface(Double useSurface) {
        this.useSurface = useSurface;
    }

    public String getBuiltSurface() {
        return this.builtSurface;
    }

    public Imob builtSurface(String builtSurface) {
        this.setBuiltSurface(builtSurface);
        return this;
    }

    public void setBuiltSurface(String builtSurface) {
        this.builtSurface = builtSurface;
    }

    public String getCompart() {
        return this.compart;
    }

    public Imob compart(String compart) {
        this.setCompart(compart);
        return this;
    }

    public void setCompart(String compart) {
        this.compart = compart;
    }

    public String getConfort() {
        return this.confort;
    }

    public Imob confort(String confort) {
        this.setConfort(confort);
        return this;
    }

    public void setConfort(String confort) {
        this.confort = confort;
    }

    public Integer getFloor() {
        return this.floor;
    }

    public Imob floor(Integer floor) {
        this.setFloor(floor);
        return this;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getNbofKitchens() {
        return this.nbofKitchens;
    }

    public Imob nbofKitchens(Integer nbofKitchens) {
        this.setNbofKitchens(nbofKitchens);
        return this;
    }

    public void setNbofKitchens(Integer nbofKitchens) {
        this.nbofKitchens = nbofKitchens;
    }

    public String getNbofBthrooms() {
        return this.nbofBthrooms;
    }

    public Imob nbofBthrooms(String nbofBthrooms) {
        this.setNbofBthrooms(nbofBthrooms);
        return this;
    }

    public void setNbofBthrooms(String nbofBthrooms) {
        this.nbofBthrooms = nbofBthrooms;
    }

    public String getUnitType() {
        return this.unitType;
    }

    public Imob unitType(String unitType) {
        this.setUnitType(unitType);
        return this;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getUnitHeight() {
        return this.unitHeight;
    }

    public Imob unitHeight(String unitHeight) {
        this.setUnitHeight(unitHeight);
        return this;
    }

    public void setUnitHeight(String unitHeight) {
        this.unitHeight = unitHeight;
    }

    public String getNbofBalconies() {
        return this.nbofBalconies;
    }

    public Imob nbofBalconies(String nbofBalconies) {
        this.setNbofBalconies(nbofBalconies);
        return this;
    }

    public void setNbofBalconies(String nbofBalconies) {
        this.nbofBalconies = nbofBalconies;
    }

    public String getUtilities() {
        return this.utilities;
    }

    public Imob utilities(String utilities) {
        this.setUtilities(utilities);
        return this;
    }

    public void setUtilities(String utilities) {
        this.utilities = utilities;
    }

    public String getFeatures() {
        return this.features;
    }

    public Imob features(String features) {
        this.setFeatures(features);
        return this;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getOtherdetails() {
        return this.otherdetails;
    }

    public Imob otherdetails(String otherdetails) {
        this.setOtherdetails(otherdetails);
        return this;
    }

    public void setOtherdetails(String otherdetails) {
        this.otherdetails = otherdetails;
    }

    public String getZoneDetails() {
        return this.zoneDetails;
    }

    public Imob zoneDetails(String zoneDetails) {
        this.setZoneDetails(zoneDetails);
        return this;
    }

    public void setZoneDetails(String zoneDetails) {
        this.zoneDetails = zoneDetails;
    }

    public String getAvailability() {
        return this.availability;
    }

    public Imob availability(String availability) {
        this.setAvailability(availability);
        return this;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    /*  public String getOwnerid() {
        return this.ownerid;
    }

    public Imob ownerid(String ownerid) {
        this.setOwnerid(ownerid);
        return this;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }
*/
    public User getImobstouser() {
        return this.imobstouser;
    }

    public void setImobstouser(User user) {
        this.imobstouser = user;
    }

    public Imob imobstouser(User user) {
        this.setImobstouser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Imob)) {
            return false;
        }
        return id != null && id.equals(((Imob) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Imob{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", categ='" + getCateg() + "'" +
            ", service='" + getService() + "'" +
            ", price=" + getPrice() +
            ", priceCurrency='" + getPriceCurrency() + "'" +
            ", tags='" + getTags() + "'" +
            ", address='" + getAddress() + "'" +
            ", contact='" + getContact() + "'" +
            ", nbofRooms=" + getNbofRooms() +
            ", constrYear='" + getConstrYear() + "'" +
            ", useSurface=" + getUseSurface() +
            ", builtSurface='" + getBuiltSurface() + "'" +
            ", compart='" + getCompart() + "'" +
            ", confort='" + getConfort() + "'" +
            ", floor=" + getFloor() +
            ", nbofKitchens=" + getNbofKitchens() +
            ", nbofBthrooms='" + getNbofBthrooms() + "'" +
            ", unitType='" + getUnitType() + "'" +
            ", unitHeight='" + getUnitHeight() + "'" +
            ", nbofBalconies='" + getNbofBalconies() + "'" +
            ", utilities='" + getUtilities() + "'" +
            ", features='" + getFeatures() + "'" +
            ", otherdetails='" + getOtherdetails() + "'" +
            ", zoneDetails='" + getZoneDetails() + "'" +
            ", availability='" + getAvailability() + "'";
           /* ", ownerid='" + getOwnerid() + "'" +
            "}";*/
    }
}
