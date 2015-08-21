package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.entities.DiscountCardHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by artemvlasov on 21/08/15.
 */
public interface DiscountCardHistoryRepository extends JpaRepository<DiscountCardHistory, Long> {
    DiscountCardHistory findById(long id);
    @Query("select dch from DiscountCardHistory dch, DiscountCard dc " +
            "where dch member of dc.discountCardHistories " +
            "and dc.id = ?1")
    List<DiscountCardHistory> findByDiscountCardId(long id);
    @Query("select dch from DiscountCardHistory dch, DiscountCard dc, User u " +
            "where dch member of dc.discountCardHistories " +
            "and dc member of u.discountCards " +
            "and u.id = ?1")
    List<DiscountCardHistory> findByUserId(long id);
}
