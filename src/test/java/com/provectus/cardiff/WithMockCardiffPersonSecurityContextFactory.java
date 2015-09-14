package com.provectus.cardiff;

import com.provectus.cardiff.config.security.CardiffUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.util.Assert;

/**
 * Created by artemvlasov on 14/09/15.
 */
public class WithMockCardiffPersonSecurityContextFactory implements WithSecurityContextFactory<WithMockCardiffPerson> {
    private CardiffUserDetailsService cardiffUserDetailsService;

    @Autowired
    public WithMockCardiffPersonSecurityContextFactory(CardiffUserDetailsService cardiffUserDetailsService) {
        this.cardiffUserDetailsService = cardiffUserDetailsService;
    }

    @Override
    public SecurityContext createSecurityContext(WithMockCardiffPerson annotation) {
        String username = annotation.value();
        Assert.hasLength(username, "value() must be non empty String");
        UserDetails principal = cardiffUserDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
