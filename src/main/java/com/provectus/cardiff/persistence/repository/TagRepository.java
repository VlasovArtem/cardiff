package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by artemvlasov on 21/08/15.
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findById(Long id);

    void deleteById (Long id);

}
