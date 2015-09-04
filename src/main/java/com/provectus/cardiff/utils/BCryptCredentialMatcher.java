package com.provectus.cardiff.utils;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Created by artemvlasov on 24/08/15.
 */
public class BCryptCredentialMatcher implements CredentialsMatcher {

    /**
     * Match password from login with password from database, that was crypted by BCrypt
     * @param token Object created during login
     * @param info Object created during successful authentication or login
     * @return true when password is matches
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String password;
        String hashed_password;

        if(token instanceof UsernamePasswordToken) {
            password = toString(((UsernamePasswordToken) token).getPassword());
            hashed_password = getCredentials(info);

            return BCrypt.checkpw(password, hashed_password);
        }

        else {
            throw new RuntimeException("You aren't passing in passwords");
        }
    }

    private String getCredentials(AuthenticationInfo info) {
        Object credentials = info.getCredentials();
        return toString(credentials);
    }

    private String toString(Object o) {
        if (o == null) {
            String msg = "Argument for String conversion cannot be null.";
            throw new IllegalArgumentException(msg);
        }
        if (o instanceof byte[]) {
            return toString((byte[]) o);
        } else if (o instanceof char[]) {
            return new String((char[]) o);
        } else if (o instanceof String) {
            return (String) o;
        } else {
            return o.toString();
        }
    }
}
