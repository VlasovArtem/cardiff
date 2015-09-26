package com.provectus.cardiff;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by artemvlasov on 20/08/15.
 */
public class JpaTest {
    private static final Logger LOGGER = LogManager.getLogger(JpaTest.class);

    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.parse("2015-08-26T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        System.out.println(localDateTime);
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("cardiff-persistence");
//        EntityManager em = emf.createEntityManager();
//        try {
//            em.getTransaction().begin();
//            em.getTransaction().commit();
//        } finally {
//            if(em.isOpen()) {
//                em.close();
//            }
//            emf.close();
//        }
        System.exit(0);
    }
}
