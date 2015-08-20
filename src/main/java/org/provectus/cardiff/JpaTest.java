package org.provectus.cardiff;

import org.provectus.cardiff.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * Created by artemvlasov on 20/08/15.
 */
public class JpaTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("cardiff-persistence");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("from User", User.class);
            query.setMaxResults(1);
            User user = (User) query.getSingleResult();
            System.out.println(user);
            System.out.println(user.getPassword().toString());
            em.getTransaction().commit();
        } finally {
            if(em.isOpen()) {
                em.close();
            }
            emf.close();
        }
    }
}
