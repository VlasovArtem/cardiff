package com.provectus.cardiff.utils.security;

import com.provectus.cardiff.WithMockCardiffPerson;
import com.provectus.cardiff.config.AppConfig;
import com.provectus.cardiff.config.CardiffAppInitializer;
import com.provectus.cardiff.config.DevelopmentDataSourceConfig;
import com.provectus.cardiff.config.RootContextConfig;
import com.provectus.cardiff.config.security.SecurityConfig;
import com.provectus.cardiff.enums.PersonRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by artemvlasov on 19/10/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        DevelopmentDataSourceConfig.class,
        CardiffAppInitializer.class,
        AppConfig.class,
        RootContextConfig.class, SecurityConfig.class})
@ActiveProfiles(profiles = "development")
@WebAppConfiguration
@SqlGroup(value = {
        @Sql("/sql-data/drop-data.sql"),
        @Sql("/sql-data/location-data.sql"),
        @Sql("/sql-data/person-data.sql")
})
public class AuthenticatedPersonPrincipalUtilTest {

    @Test
    @WithMockCardiffPerson(value = "vadimguliaev")
    public void getAuthenticationPrincipalTest() {
        assertTrue(AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal().isPresent());
    }

    @Test
    public void getAuthenticationPrincipalPersonIsNotAuthenticatedTest() {
        assertFalse(AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal().isPresent());
    }

    @Test
    @WithMockCardiffPerson(value = "vadimguliaev")
    public void containAuthoritiesTest() {
        assertTrue(AuthenticatedPersonPrincipalUtil.containAuthorities(PersonRole.USER));
    }

    @Test
    @WithMockCardiffPerson(value = "vadimguliaev")
    public void containAuthoritiesWithoutRequiredRoleTest() {
        assertFalse(AuthenticatedPersonPrincipalUtil.containAuthorities(PersonRole.ADMIN));
    }

    @Test
    @WithMockCardiffPerson(value = "dmitriyvalnov")
    public void containAuthoritiesWithDifferentRolesTest() {
        assertTrue(AuthenticatedPersonPrincipalUtil.containAuthorities(PersonRole.ADMIN, PersonRole.USER));
    }
}
