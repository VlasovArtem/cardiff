package com.provectus.cardiff.service;

import com.provectus.cardiff.entities.Location;

import java.util.List;

/**
 * Created by artemvlasov on 09/09/15.
 */
public interface LocationService {
    List<Location> getAll();
    boolean exists(String city, String country);
    Location find(String city, String country);
    long count();

}
