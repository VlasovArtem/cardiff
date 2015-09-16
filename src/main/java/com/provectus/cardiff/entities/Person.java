package com.provectus.cardiff.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.provectus.cardiff.enums.PersonRole;
import com.provectus.cardiff.utils.converter.PasswordConverter;
import com.provectus.cardiff.utils.view.PersonView;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.util.Set;

/**
 * Created by artemvlasov on 19/08/15.
 */
@Entity
@Table(name = "person")
@NamedEntityGraph(name = "Person.discountCards",
        attributeNodes = @NamedAttributeNode(
                value = "discountCards",
                subgraph = "tagsGraph"),
        subgraphs = @NamedSubgraph(
                name = "tagsGraph",
                attributeNodes = {@NamedAttributeNode(value = "tags")})
)
@Access(AccessType.PROPERTY)
public class Person extends BaseEntity {
    private String name;
    private String login;
    private String password;
    private String email;
    private long phoneNumber;
    private String description;
    private PersonRole role;
    private boolean deleted;
    private Set<DiscountCard> discountCards;
    private Location location;

    public Person() {
    }

    public Person(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
    }

    @Column(length = 100)
    @JsonView(PersonView.BasicLevel.class)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 100, unique = true, nullable = false)
    @JsonView(PersonView.TableLevel.class)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @JsonIgnore
    @Column(nullable = false, columnDefinition = "bytea")
    @Convert(converter = PasswordConverter.class)
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @Column(length = 50, unique = true, nullable = false)
    @JsonView(PersonView.BasicLevel.class)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "phone_number", nullable = false)
    @JsonView(PersonView.BasicLevel.class)
    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(length = 500)
    @JsonView(PersonView.TableLevel.class)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonView(PersonView.TableLevel.class)
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "owner")
    @JsonView(PersonView.DiscountCardsLevel.class)
    public Set<DiscountCard> getDiscountCards() {
        return discountCards;
    }

    public void setDiscountCards(Set<DiscountCard> discountCards) {
        this.discountCards = discountCards;
    }

    @Column(name = "role", columnDefinition = "varchar(9) default 'USER'")
    @Enumerated(EnumType.STRING)
    @JsonView(PersonView.TableLevel.class)
    public PersonRole getRole() {
        return role;
    }

    public void setRole(PersonRole role) {
        this.role = role;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id", referencedColumnName = "id", nullable = false)
    @JsonView(PersonView.BasicLevel.class)
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @PrePersist
    public void setPersonRole() {
        role = PersonRole.USER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;

        Person person = (Person) o;

        if (phoneNumber != person.phoneNumber) return false;
        if (deleted != person.deleted) return false;
        if (name != null ? !name.equals(person.name) : person.name != null) return false;
        if (!login.equals(person.login)) return false;
        if (!password.equals(person.password)) return false;
        if (!email.equals(person.email)) return false;
        if (description != null ? !description.equals(person.description) : person.description != null) return false;
        if (role != person.role) return false;
        return !(discountCards != null ? !discountCards.equals(person.discountCards) : person.discountCards != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + login.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + (int) (phoneNumber ^ (phoneNumber >>> 32));
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + (discountCards != null ? discountCards.hashCode() : 0);
        return result;
    }
}
