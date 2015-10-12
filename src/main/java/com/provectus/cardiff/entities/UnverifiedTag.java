package com.provectus.cardiff.entities;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by Дмитрий on 04/10/15.
 */
@Entity
@Table(name = "unferified_tag")
@Access(AccessType.PROPERTY)
public class UnverifiedTag {
    private long id;
    private String tag;
    private LocalDate createdDate;

    public UnverifiedTag() {
    }

    public UnverifiedTag(String tag) {
        this.tag = tag;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
    return id;
}

    public void setId(long id) {
        this.id = id;
    }

    @Column(length = 30, unique = true, nullable = false)
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Column(name = "crated_date")
    public LocalDate getDate() {
        return createdDate;
    }
    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                '}';
    }
}
