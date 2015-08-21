package com.provectus.cardiff.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Created by artemvlasov on 20/08/15.
 */
@Entity
@Table(name = "book_card")
@AttributeOverride(name = "createdDate",
        column = @Column(name = "book_date_start", insertable = false, updatable = false))
public class BookCard extends BaseEntity {
    @Column(name = "book_date_end")
    private LocalDateTime bookDateEnd;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_card_id")
    private DiscountCard discountCard;

    public LocalDateTime getBookDateEnd() {
        return bookDateEnd;
    }

    public void setBookDateEnd(LocalDateTime bookDateEnd) {
        this.bookDateEnd = bookDateEnd;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DiscountCard getDiscountCard() {
        return discountCard;
    }

    public void setDiscountCard(DiscountCard discountCard) {
        this.discountCard = discountCard;
    }

    @Override
    public String toString() {
        return "BookCard{" +
                ", bookDateEnd=" + bookDateEnd +
                ", user=" + user +
                ", discountCard=" + discountCard +
                '}';
    }
}
