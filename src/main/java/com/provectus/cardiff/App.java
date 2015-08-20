package com.provectus.cardiff;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import com.provectus.cardiff.entities.User;

import java.util.List;

/**
 * Created by artemvlasov on 19/08/15.
 */
public class App {
    public final SessionFactory sessionFactory;
    public Session session;
    public Transaction tx;

    public App() {
        sessionFactory = buildSessionFactory();
    }

    public static void main(String[] args) {
        App app = new App();
        app.saveUser(new User("Vlasov Artem", "vlasovartem", "password".getBytes()));
        System.exit(0);
    }
    private SessionFactory buildSessionFactory() {
        Configuration conf = new Configuration();
        conf.configure();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
        return conf.buildSessionFactory(serviceRegistry);
    }
    public void saveUser(User user) {
        session = sessionFactory.openSession();
        tx = session.beginTransaction();
        session.save(user);
        session.flush();
        tx.commit();
        session.close();
    }
    public User getUser(long id) {
        session = sessionFactory.openSession();
        session.beginTransaction();
        User user = (User) session.get(User.class, id);
        session.close();
        return user;
    }
    public List<User> getUsers() {
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            List<User> users = (List<User>) session.createQuery("from org.provectus.cardiff.entities.User").list();
            return users;
        } finally {
            session.close();
        }
    }
}
