package com.provectus.cardiff.utils.security;

import com.provectus.cardiff.config.security.CardiffUserDetails;
import com.provectus.cardiff.enums.PersonRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by artemvlasov on 13/09/15.
 */
public class AuthenticatedPersonPrincipalUtil {
    public static Optional<CardiffUserDetails> getAuthenticationPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            if(authentication.getPrincipal() instanceof CardiffUserDetails) {
                return Optional.of(((CardiffUserDetails) authentication.getPrincipal()));
            }
        }
        return Optional.empty();
    }
    public static boolean containAuthorities(PersonRole... roles) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .allMatch(p -> Arrays.asList(roles).contains(PersonRole.valueOf(p.getAuthority())));
    }
}
