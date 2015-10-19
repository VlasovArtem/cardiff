package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.entities.CustomTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Дмитрий on 04/10/15.
 */
public interface CustomTagRepository extends JpaRepository<CustomTag, Long> {

    CustomTag findById(Long id);

    void deleteById(Long id);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM CustomTag t WHERE upper(t.tag) = upper(?1)")
    boolean existsByTag(String tag);

    long countByAuthorId(long id);

}
