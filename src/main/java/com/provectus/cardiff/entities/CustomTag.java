package com.provectus.cardiff.entities;

import javax.persistence.*;

/**
 * Created by Дмитрий on 04/10/15.
 */
@Entity
@Table(name = "custom_tag")
@Access(AccessType.PROPERTY)
public class CustomTag extends BaseEntity {
    private String tag;
    private String description;
    private Person author;

    public CustomTag() {
    }

    public CustomTag(String tag) {
        this.tag = tag;
    }

    @Column(length = 20, unique = true, nullable = false)
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Column(length = 50)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }
}
