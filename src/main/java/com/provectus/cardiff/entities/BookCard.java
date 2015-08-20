package com.provectus.cardiff.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Created by artemvlasov on 20/08/15.
 */
@Entity
@Table(name = "book_card")
public class BookCard {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    public long id;
    @Column(name = "book_date_start")
    private LocalDateTime bookDateStart;
    @Column(name = "book_date_end")
    private LocalDateTime bookDateEnd;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_card_id")
    private DiscountCard discountCard;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getBookDateStart() {
        return bookDateStart;
    }

    public void setBookDateStart(LocalDateTime bookDateStart) {
        this.bookDateStart = bookDateStart;
    }

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
                "id=" + id +
                ", bookDateStart=" + bookDateStart +
                ", bookDateEnd=" + bookDateEnd +
                ", user=" + user +
                ", discountCard=" + discountCard +
                '}';
    }
}
