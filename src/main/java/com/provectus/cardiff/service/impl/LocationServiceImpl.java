package com.provectus.cardiff.service.impl;

import com.provectus.cardiff.entities.Location;
import com.provectus.cardiff.persistence.repository.LocationRepository;
import com.provectus.cardiff.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by artemvlasov on 09/09/15.
 */
@Service
@Transactional(readOnly = true)
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepository repository;

    @Override
    public List<Location> getAll() {
        Sort sort = new Sort(Sort.Direction.DESC, "country", "city");
        return repository.findAll(sort);
    }

    @Override
    public boolean exists(String city, String country) {
        return repository.existsByCityAndCountry(city, country);
    }

    @Override
    public Location find(String city, String country) {
        return repository.findByCityAndCountry(city, country);
    }
}
