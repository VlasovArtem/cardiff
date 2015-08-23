package com.provectus.cardiff.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.provectus.cardiff.utils.View;
import com.provectus.cardiff.utils.converters.PasswordConverter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.List;

/**
 * Created by artemvlasov on 19/08/15.
 */
@Entity
@Table(name = "client")
@NamedQueries({
        @NamedQuery(name = "User.existsByLogin",
                query = "SELECT CASE WHEN (COUNT(p) > 0) THEN true ELSE false END FROM User p WHERE p.login = ?1"),
        @NamedQuery(name = "User.existsByEmail",
                query = "SELECT CASE WHEN (COUNT(p) > 0) THEN true ELSE false END FROM User p WHERE p.email = ?1"),
})
@NamedEntityGraph(name = "User.discountCards", attributeNodes = @NamedAttributeNode("discountCards"))
public class User extends BaseEntity{
    @Column(length = 100)
    private String name;
    @Column(length = 100, unique = true, nullable = false)
    private String login;
    @Column(nullable = false, columnDefinition = "bytea")
    @Convert(converter = PasswordConverter.class)
    private String password;
    @Column(length = 50, unique = true, nullable = false)
    private String email;
    @Column(name = "phone_number", nullable = false)
    private long phoneNumber;
    @Column(length = 500)
    private String description;
    private boolean deleted;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonView(View.SecondLevel.class)
    private List<DiscountCard> discountCards;

    public User() {
    }

    public User(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
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

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
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

    public List<DiscountCard> getDiscountCards() {
        return discountCards;
    }

    public void setDiscountCards(List<DiscountCard> discountCards) {
        this.discountCards = discountCards;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", description='" + description + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
