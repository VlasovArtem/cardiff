package com.provectus.cardiff;

import com.provectus.cardiff.utils.converter.PasswordConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Created by artemvlasov on 20/08/15.
 */
public class JpaTest {
    private static final Logger LOGGER = LogManager.getLogger(JpaTest.class);

    public static void main(String[] args) {
        System.out.println(new PasswordConverter().convertToDatabaseColumn(BCrypt.hashpw("testpassword", BCrypt
                .gensalt())).toString());
        System.out.println(new PasswordConverter().convertToDatabaseColumn(BCrypt.hashpw("testpassword2", BCrypt
                .gensalt())));
        System.out.println(new PasswordConverter().convertToDatabaseColumn(BCrypt.hashpw("testpassword3", BCrypt
                .gensalt())));
    }
}
