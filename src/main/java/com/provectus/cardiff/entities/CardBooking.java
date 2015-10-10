package com.provectus.cardiff.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonView;
import com.provectus.cardiff.utils.view.CardBookingView;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Created by artemvlasov on 20/08/15.
 */
@Entity
@Table(name = "card_booking")
@Access(AccessType.PROPERTY)
@JsonAutoDetect
@NamedEntityGraphs(value = {
        @NamedEntityGraph(
                name = "CardBooking.personBookedTable",
                attributeNodes = {
                        @NamedAttributeNode("discountCard")
                }
        ),@NamedEntityGraph(
                name = "CardBooking.personBookingsTable",
                attributeNodes = {
                        @NamedAttributeNode("person"),
                        @NamedAttributeNode("discountCard")
                }
        )
})
public class CardBooking {

    private long id;
    private LocalDate bookingStartDate;
    private LocalDate bookingEndDate;
    private Person person;
    private DiscountCard discountCard;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "booking_start_date")
    public LocalDate getBookingStartDate() {
        return bookingStartDate;
    }

    public void setBookingStartDate(LocalDate bookingStartDate) {
        this.bookingStartDate = bookingStartDate;
    }

    @Column(name = "booking_end_date")
    public LocalDate getBookingEndDate() {
        return bookingEndDate;
    }

    public void setBookingEndDate(LocalDate bookingEndDate) {
        this.bookingEndDate = bookingEndDate;
    }

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    @JsonView(CardBookingView.BookingsLevel.class)
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_card_id")
    public DiscountCard getDiscountCard() {
        return discountCard;
    }

    public void setDiscountCard(DiscountCard discountCard) {
        this.discountCard = discountCard;
    }

    /**
     * Set Book Date End plus 6 days from now.
     */
    @PrePersist
    public void setBookingEndDate() {
        bookingEndDate = bookingStartDate == null ? LocalDate.now().plusDays(6l) : bookingStartDate.plusDays(6l);
    }
}
