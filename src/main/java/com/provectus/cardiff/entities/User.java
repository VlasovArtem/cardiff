package com.provectus.cardiff.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonView;
import com.provectus.cardiff.utils.View;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Created by artemvlasov on 19/08/15.
 */
@Entity
@Table(name = "\"user\"")
@JsonAutoDetect
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;
    @Column(length = 100)
    private String name;
    @Column(length = 100, unique = true)
    private String login;
    private byte[] password;
    @Column(length = 50, unique = true)
    private String email;
    @Column(name = "phone_number")
    private long phoneNumber;
    @Column(length = 500)
    private String description;
    private boolean deleted;
    @Column(name = "created_date")
    @JsonView(View.FirstLevel.class)
    private LocalDateTime createdDate;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<DiscountCard> discountCards;

    public User() {
    }

    public User(String name, String login, byte[] password) {
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<DiscountCard> getDiscountCards() {
        return discountCards;
    }

    public void setDiscountCards(List<DiscountCard> discountCards) {
        this.discountCards = discountCards;
    }

    /**
     * Set current date and time before persist object into database.
     */
    @PrePersist
    public void putCreatedDate() {
        createdDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", password=" + Arrays.toString(password) +
                ", email='" + email + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", description='" + description + '\'' +
                ", deleted=" + deleted +
                ", createdDate=" + createdDate +
//                ", discountCards=" + discountCards +
                '}';
    }
}
