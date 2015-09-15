package com.provectus.cardiff.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Created by artemvlasov on 12/09/15.
 */
public class CardiffUserDetails extends User {

    private long id;

    public CardiffUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
                              long id) {
        super(username, password, authorities);
        this.id = id;
    }

    public CardiffUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public long getId() {
        return id;
    }
}
