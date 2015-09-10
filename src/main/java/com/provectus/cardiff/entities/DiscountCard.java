package com.provectus.cardiff.entities;

import com.fasterxml.jackson.annotation.JsonView;
import com.provectus.cardiff.utils.view.DiscountCardView;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Created by artemvlasov on 20/08/15.
 */
@Entity
@Table(name = "discount_card")
@NamedEntityGraph(name = "DiscountCard.discountCardComments", attributeNodes = @NamedAttributeNode("discountCardComments"))
public class DiscountCard extends BaseEntity {
    @Column(name = "card_number", length = 16, unique = true, nullable = false)
    private long cardNumber;
    @Column(name = "expired_date")
    private LocalDateTime expiredDate;
    @Column(columnDefinition = "boolean default true")
    private boolean available;
    @Column(name = "company_name", nullable = false)
    private String companyName;
    @Column(name = "amount_of_discount", length = 3)
    private int amountOfDiscount;
    @Column(length = 500)
    private String description;
    private boolean deleted;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "tag_card",
            joinColumns = @JoinColumn(name = "discount_card_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "discount_card_id", referencedColumnName = "id")
    @JsonView(DiscountCardView.DiscountCardCommentsLevel.class)
    private List<DiscountCardComment> discountCardComments;

    public DiscountCard() {
    }

    public DiscountCard(long cardNumber, String companyName) {
        this.cardNumber = cardNumber;
        this.companyName = companyName;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDateTime getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(LocalDateTime expiredDate) {
        this.expiredDate = expiredDate;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getAmountOfDiscount() {
        return amountOfDiscount;
    }

    public void setAmountOfDiscount(int amountOfDiscount) {
        this.amountOfDiscount = amountOfDiscount;
    }

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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public List<DiscountCardComment> getDiscountCardComments() {
        return discountCardComments;
    }

    public void setDiscountCardComments(List<DiscountCardComment> discountCardComments) {
        this.discountCardComments = discountCardComments;
    }
}
