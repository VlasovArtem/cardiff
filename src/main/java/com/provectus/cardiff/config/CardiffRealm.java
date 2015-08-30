package com.provectus.cardiff.config;

import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.enums.PersonRole;
import com.provectus.cardiff.persistence.repository.PersonRepository;
import com.provectus.cardiff.utils.BCryptCredentialMatcher;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by artemvlasov on 24/08/15.
 */
@Component
public class CardiffRealm extends AuthorizingRealm {
    @Autowired
    private PersonRepository userRepository;

    public CardiffRealm() {
        setName("CardiffRealm");
        setCredentialsMatcher(new BCryptCredentialMatcher());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Long id = (Long) principals.fromRealm(getName()).iterator().next();
        Optional<Person> user = Optional.ofNullable(userRepository.findById(id));
        if(user.isPresent()) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            for (PersonRole userRole : PersonRole.values()) {
                if(user.get().getRole() == userRole) {
                    info.addRole(userRole.name());
                }
            }
            return info;
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String tokenUsername = token.getUsername();
        Optional<Person> user = Optional.ofNullable(userRepository.findByEmailOrLoginAndDeleted(tokenUsername,
                tokenUsername, false));
        if(user.isPresent()) {
            return new SimpleAuthenticationInfo(user.get().getId(), user.get().getPassword(), getName());
        }
        return null;
    }
}
