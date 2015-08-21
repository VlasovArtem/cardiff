package com.provectus.cardiff;

import com.provectus.cardiff.entities.BookCard;
import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.entities.Tag;
import com.provectus.cardiff.entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by artemvlasov on 20/08/15.
 */
public class JpaTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("cardiff-persistence");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            User user = createUser();
            em.persist(user);
            DiscountCard discountCard = createDiscountCard(5456523696535238l);
            user.setDiscountCards(Collections.singletonList(discountCard));
            em.persist(discountCard);
            BookCard bc = createBookCard();
            bc.setDiscountCard(discountCard);
            bc.setUser(user);
            em.persist(bc);
            Tag tag1 = createTag("First");
            Tag tag2 = createTag("Second");
            discountCard.setTags(Stream.of(tag1, tag2).collect(Collectors.toSet()));
            em.persist(tag1);
            em.persist(tag2);
            em.getTransaction().commit();
        } finally {
            if(em.isOpen()) {
                em.close();
            }
            emf.close();
        }
        System.exit(0);
    }
    private static User createUser() {
        User user = new User();
        user.setPhoneNumber(5469896);
        user.setCreatedDate(LocalDateTime.now());
        user.setDescription("Description for this user");
        user.setEmail("testemail@mail.com");
        user.setLogin("testlogin");
        user.setName("Test name");
        user.setPassword("password".getBytes());
        return user;
    }
    private static DiscountCard createDiscountCard(long cardNumber) {
        DiscountCard discountCard = new DiscountCard();
        discountCard.setDescription("This is the first discount card");
        discountCard.setAmountOfDiscount(10);
        discountCard.setCardNumber(cardNumber);
        discountCard.setCompanyName("Robin Bobin");
        discountCard.setExpiredDate(LocalDateTime.now().plusYears(2l));
        return discountCard;
    }
    public static BookCard createBookCard() {
        BookCard bookCard = new BookCard();
        bookCard.setBookDateEnd(LocalDateTime.now().plusDays(5));
        return bookCard;
    }
    public static Tag createTag(String myTag) {
        Tag tag = new Tag();
        tag.setTag(myTag);
        return tag;
    }

}
