package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by artemvlasov on 09/09/15.
 */
public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findByCityAndCountry(String city, String country);

    @Query("SELECT CASE WHEN (COUNT(l) > 0) THEN true ELSE false END FROM Location l WHERE l.city = ?1 AND l.country = ?2")
    boolean existsByCityAndCountry(String city, String country);
}
