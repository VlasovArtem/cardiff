package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.entities.UnverifiedTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Дмитрий on 04/10/15.
 */
public interface UnverifiedTagRepository extends JpaRepository<UnverifiedTag, Long> {

    UnverifiedTag findById(Long id);

    void deleteById(Long id);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM UnverifiedTag t WHERE t.tag = ?1")
    boolean existsByTag(String tag);
}
