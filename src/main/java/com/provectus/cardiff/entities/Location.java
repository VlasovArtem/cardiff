package com.provectus.cardiff.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by artemvlasov on 08/09/15.
 */
@Entity
@Table(name = "location")
public class Location extends BaseEntity {
    protected String city;
    protected String country;

    public Location() {
    }

    public Location(String city, String country) {
        this.city = city;
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "Location{" +
                "city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
