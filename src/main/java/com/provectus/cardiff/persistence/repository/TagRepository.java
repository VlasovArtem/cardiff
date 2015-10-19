package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by artemvlasov on 21/08/15.
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Tag t WHERE t.tag = ?1")
    boolean existsByTag(String tag);
}
