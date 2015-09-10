package com.provectus.cardiff;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * Created by artemvlasov on 20/08/15.
 */
public class JpaTest {
    private static final Logger LOGGER = LogManager.getLogger(JpaTest.class);

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("cardiff-persistence");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT p FROM Person p join p.discountCards dc WHERE dc.id = ?1");
            query.setParameter(1, 1l);
            Object o = query.getSingleResult();
            em.getTransaction().commit();
        } finally {
            if(em.isOpen()) {
                em.close();
            }
            emf.close();
        }
        System.exit(0);
    }
}
