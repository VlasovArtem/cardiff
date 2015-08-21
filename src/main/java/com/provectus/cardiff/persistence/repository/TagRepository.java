package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by artemvlasov on 21/08/15.
 */
public interface TagRepository extends JpaRepository<Tag, Long> {
}
