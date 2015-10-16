package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.config.AppConfig;
import com.provectus.cardiff.config.DevelopmentDataSourceConfig;
import com.provectus.cardiff.config.RootContextConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * Created by artemvlasov on 16/10/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DevelopmentDataSourceConfig.class, AppConfig.class, RootContextConfig.class})
@ActiveProfiles(profiles = "development")
@SqlGroup(value = {
        @Sql("/sql-data/drop-data.sql"),
        @Sql("/sql-data/location-data.sql")
})
@DirtiesContext
@WebAppConfiguration
@Transactional
public class LocationRepositoryTest {
    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void findByCityAndCountryTest() {
        assertNotNull(locationRepository.findByCityAndCountryIgnoreCase("Odessa", "Ukraine"));
    }

    @Test
    public void findByCityAndCountryWithNotMatchesDataTest() {
        assertNull(locationRepository.findByCityAndCountryIgnoreCase("Kiev", "Ukraine"));
    }

    @Test
    public void existsByCityAndCountryTest() {
        assertTrue(locationRepository.existsByCityAndCountry("Odessa", "Ukraine"));
    }

    @Test
    public void existsByCityAndCountryWithNotMatchesDataTest() {
        assertFalse(locationRepository.existsByCityAndCountry("Kiev", "Ukraine"));
    }
}
