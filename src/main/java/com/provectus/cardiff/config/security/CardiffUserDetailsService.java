package com.provectus.cardiff.config.security;

import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.enums.PersonRole;
import com.provectus.cardiff.persistence.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by artemvlasov on 12/09/15.
 */
@Component
public class CardiffUserDetailsService implements UserDetailsService {
    private final int PROJECT_AUTHORITIES_COUNT = PersonRole.values().length;

    @Autowired
    private PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByEmailOrLogin(username, username);
        if(person == null) {
            throw new UsernameNotFoundException("Person with login data is not found");
        }
        Set<GrantedAuthority> authorities = new HashSet<>(PROJECT_AUTHORITIES_COUNT);
        if(PersonRole.ADMIN.equals(person.getRole())) {
            authorities.add(new SimpleGrantedAuthority(PersonRole.ADMIN.name()));
        }
            authorities.add(new SimpleGrantedAuthority(PersonRole.USER.name()));
        return new CardiffUserDetails(person.getLogin(), person.getPassword(), authorities, person.getId());
    }
}
