package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.entities.Person;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by artemvlasov on 20/08/15.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{
    @EntityGraph(value = "Person.discountCards", type = EntityGraph.EntityGraphType.LOAD)
    Person findById(long id);
    Person findByLogin(String login);
    Person findByEmail(String email);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    boolean hasRole(long id, String role);
    boolean existsByLoginOrEmail(String login, String email);
    Person findByEmailOrLogin(String email, String login);
}
