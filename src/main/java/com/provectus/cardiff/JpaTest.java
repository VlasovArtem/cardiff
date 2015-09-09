package com.provectus.cardiff;

import com.provectus.cardiff.entities.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
            Person person = em.find(Person.class, 1l);
            System.out.println(person.getLocation());
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
