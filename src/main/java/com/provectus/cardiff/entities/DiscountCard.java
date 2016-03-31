package com.provectus.cardiff.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.provectus.cardiff.utils.view.CardBookingView;
import com.provectus.cardiff.utils.view.DiscountCardView;
import com.provectus.cardiff.utils.view.PersonView;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Set;

/**
 * Created by artemvlasov on 20/08/15.
 */
@Entity
@Table(name = "discount_card")
@NamedEntityGraphs(value = {
        @NamedEntityGraph(
                name = "DiscountCard.discountCardInfo",
                attributeNodes = {
                        @NamedAttributeNode("discountCardComments")
                }
        )
})
@Access(AccessType.PROPERTY)
public class DiscountCard extends BaseEntity {
    private long cardNumber;
    private String companyName;
    private int amountOfDiscount;
    private String description;
    private boolean deleted;
    private Set<Tag> tags;
    @JsonView(value = {DiscountCardView.DiscountCardInfoLevel.class, PersonView.DiscountCardCommentsLevel.class,
            CardBookingView.InfoLevel.class})
    private Set<DiscountCardComment> discountCardComments;
    private Person owner;
    private boolean picked;
    private Location location;

    public DiscountCard() {
    }

    public DiscountCard(long cardNumber, String companyName) {
        this.cardNumber = cardNumber;
        this.companyName = companyName;
    }

    @Column(name = "card_number", length = 16, unique = true, nullable = false)
    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Column(name = "company_name", nullable = false)
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Column(name = "amount_of_discount", length = 3)
    public int getAmountOfDiscount() {
        return amountOfDiscount;
    }

    public void setAmountOfDiscount(int amountOfDiscount) {
        this.amountOfDiscount = amountOfDiscount;
    }

    @Column(length = 150)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(name = "tag_card",
            joinColumns = @JoinColumn(name = "discount_card_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "discount_card_id", referencedColumnName = "id")
    public Set<DiscountCardComment> getDiscountCardComments() {
        return discountCardComments;
    }

    public void setDiscountCardComments(Set<DiscountCardComment> discountCardComments) {
        this.discountCardComments = discountCardComments;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    @JsonIgnore
    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public boolean isPicked() {
        return picked;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountCard that = (DiscountCard) o;
        return Objects.equals(cardNumber, that.cardNumber) &&
                Objects.equals(companyName, that.companyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, companyName);
    }
}
