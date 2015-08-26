package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.entities.DiscountCardComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by artemvlasov on 21/08/15.
 */
public interface DiscountCardCommentRepository extends JpaRepository<DiscountCardComment, Long> {
    DiscountCardComment findById(long id);
    @Query("select dcc from DiscountCard dc, DiscountCardComment dcc " +
            "where dcc member of dc.discountCardComments " +
            "and dc.id = ?1")
    List<DiscountCardComment> findByDiscountCardId(long id);
    @Query("select dcc from DiscountCard dc, DiscountCardComment dcc, Person u " +
            "where dcc member of dc.discountCardComments " +
            "and dc member of u.discountCards " +
            "and u.id = ?1")
    List<DiscountCardComment> findByUserId(long id);
}
