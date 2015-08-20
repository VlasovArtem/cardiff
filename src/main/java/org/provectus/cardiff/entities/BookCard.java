package org.provectus.cardiff.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by artemvlasov on 20/08/15.
 */
@Entity
@Table(name = "book_card")
public class BookCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    @Column(name = "book_date_start")
    private Date bookDateStart;
    @Column(name = "book_date_end")
    private Date bookDateEnd;
    @ManyToOne
    private User user;
    @ManyToOne
    private DiscountCard discountCard;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getBookDateStart() {
        return bookDateStart;
    }

    public void setBookDateStart(Date bookDateStart) {
        this.bookDateStart = bookDateStart;
    }

    public Date getBookDateEnd() {
        return bookDateEnd;
    }

    public void setBookDateEnd(Date bookDateEnd) {
        this.bookDateEnd = bookDateEnd;
    }
}
