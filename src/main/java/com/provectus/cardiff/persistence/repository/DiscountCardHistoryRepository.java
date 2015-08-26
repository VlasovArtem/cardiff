package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.entities.DiscountCardHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by artemvlasov on 21/08/15.
 */
public interface DiscountCardHistoryRepository extends JpaRepository<DiscountCardHistory, Long> {
    DiscountCardHistory findById(long id);
    List<DiscountCardHistory> findByDiscountCardId(long id);
    List<DiscountCardHistory> findByPersonId(long id);
}
