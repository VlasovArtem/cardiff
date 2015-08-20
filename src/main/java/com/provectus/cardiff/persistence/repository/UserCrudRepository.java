package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by artemvlasov on 20/08/15.
 */
@Repository
public interface UserCrudRepository extends JpaRepository<User, Long>{
    User findById(long id);
    User findByLogin(String login);
    User findByEmail(String email);
    void deleteByLogin(String login);
    void deleteByEmail(String email);
}
