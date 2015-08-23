package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by artemvlasov on 20/08/15.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    @EntityGraph(value = "User.discountCards", type = EntityGraph.EntityGraphType.LOAD)
    User findById(long id);
    User findByLogin(String login);
    User findByEmail(String email);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    User findByEmailOrLogin(String email, String login);
    void deleteByLogin(String login);
    void deleteByEmail(String email);
}
