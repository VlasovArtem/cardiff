package com.provectus.cardiff;

import com.provectus.cardiff.entities.Person;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

/**
 * Created by artemvlasov on 20/08/15.
 */
public class JpaTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("cardiff-persistence");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            for (int i = 0; i < 20; i++) {
                Person person = new Person();
                person.setPassword("testuser" + i);
                person.setEmail("testuser" + i + "@mail.com");
                person.setLogin("testuser" + i);
                person.setName("test user " + i);
                person.setPhoneNumber(637472072 + i);
                if(i % 2 == 0) {
                    person.setCreatedDate(LocalDateTime.now().plusMinutes(i * 10));
                } else {
                    person.setCreatedDate(LocalDateTime.now().minusMinutes(i * 10));
                }
                em.persist(person);
            }
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
