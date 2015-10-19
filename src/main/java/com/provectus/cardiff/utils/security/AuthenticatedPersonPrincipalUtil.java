package com.provectus.cardiff.utils.security;

import com.provectus.cardiff.config.security.CardiffUserDetails;
import com.provectus.cardiff.enums.PersonRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by artemvlasov on 13/09/15.
 */
public class AuthenticatedPersonPrincipalUtil {
    public static Optional<CardiffUserDetails> getAuthenticationPrincipal() {
        if(SecurityContextHolder.getContext() != null) {
            Optional<Authentication> authentication = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
            if(authentication.isPresent()) {
                if(authentication.get().isAuthenticated()) {
                    if(authentication.get().getPrincipal() instanceof CardiffUserDetails) {
                        return Optional.of((CardiffUserDetails) authentication.get().getPrincipal());
                    }
                }
            }
        }
        return Optional.empty();
    }
    public static boolean containAuthorities(PersonRole... roles) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(p -> Arrays.asList(roles).contains(PersonRole.valueOf(p.getAuthority())));
    }
}
