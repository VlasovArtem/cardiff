package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.enums.PersonRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by artemvlasov on 20/08/15.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{
//    @EntityGraph(value = "Person.discountCards", type = EntityGraph.EntityGraphType.LOAD)
    @Query("from Person p where p.id = ?1")
    Person findById(long id);

    Person findByLogin(String login);

    Person findByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Person p WHERE p.login = ?1")
    boolean existsByLogin(String login);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Person p WHERE p.email = ?1")
    boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Person p WHERE p.phoneNumber = ?1")
    boolean existsByPhoneNumber(long phoneNumber);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Person p WHERE p.id = ?1 AND p.role = ?2")
    boolean existsByIdAndRole(long id, PersonRole role);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Person p WHERE (p.login = ?1 OR p.email = ?2) AND p.deleted = ?3")
    boolean existsByLoginOrEmailAndDeleted(String login, String email, boolean deleted);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Person p WHERE p.login = ?1 OR p.email = ?2")
    boolean existsByLoginOrEmail(String login, String email);

    Person findByEmailOrLogin(String email, String login);

    @Query("FROM Person p WHERE (p.email = ?1 OR p.login = ?2) AND p.deleted = ?3")
    Person findByEmailOrLoginAndDeleted(String email, String login, boolean deleted);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Person p WHERE p.login = ?1 OR p.email = ?2 OR p.phoneNumber = ?3")
    boolean existsByLoginOrEmailOrPhoneNumber(String login, String email, long phoneNumber);

    /**
     * Find {@link Person} with only one {@link com.provectus.cardiff.entities.DiscountCard} in Person Discount card
     * list.
     * @param discountCardId Id of the discount card
     * @return Return {@link Person} with one find
     */
    @Query("SELECT p FROM Person p, DiscountCard dc WHERE dc MEMBER OF p.discountCards AND dc.id = ?1 AND p.deleted = false")
    Person findByDiscountCardId(long discountCardId);

}
