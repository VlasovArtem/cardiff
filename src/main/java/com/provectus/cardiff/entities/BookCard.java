package com.provectus.cardiff.entities;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Created by artemvlasov on 20/08/15.
 */
@Entity
@Table(name = "book_card")
@AttributeOverride(name = "createdDate",
        column = @Column(name = "book_date_start", insertable = false, updatable = false))
@Access(AccessType.PROPERTY)
public class BookCard extends BaseEntity {
    private LocalDateTime bookDateEnd;
    private Person person;
    private DiscountCard discountCard;

    @Column(name = "book_date_end")
    public LocalDateTime getBookDateEnd() {
        return bookDateEnd;
    }

    public void setBookDateEnd(LocalDateTime bookDateEnd) {
        this.bookDateEnd = bookDateEnd;
    }

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_card_id")
    public DiscountCard getDiscountCard() {
        return discountCard;
    }

    public void setDiscountCard(DiscountCard discountCard) {
        this.discountCard = discountCard;
    }

    /**
     * Set Book Date End plus 7 days from now.
     */
    @PrePersist
    public void setBookDateEnd() {
        bookDateEnd = getCreatedDate() == null ? LocalDateTime.now().plusDays(7l) : getCreatedDate().plusDays(7l);
    }
}
