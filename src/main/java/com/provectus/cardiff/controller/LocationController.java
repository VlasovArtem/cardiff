package com.provectus.cardiff.controller;

import com.provectus.cardiff.entities.Location;
import com.provectus.cardiff.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by artemvlasov on 09/09/15.
 */
@RestController
@RequestMapping("/rest/location")
public class LocationController {
    @Autowired
    private LocationService locationService;

    @RequestMapping(path = "/get/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Location> getAll() {
        return locationService.getAll();
    }
}
