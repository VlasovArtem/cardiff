package org.provectus.cardiff.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by artemvlasov on 19/08/15.
 */
@Entity(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String login;
    private byte[] password;
    private String email;
    @Column(name = "phone_number")
    private long phoneNumber;
    private String description;
    private boolean deleted;
    @Column(name = "created_date")
    private Date createdDate;

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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
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
                '}';
    }
}
