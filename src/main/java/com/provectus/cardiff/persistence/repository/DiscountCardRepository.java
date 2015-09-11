package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.entities.DiscountCard;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by artemvlasov on 21/08/15.
 */
public interface DiscountCardRepository extends JpaRepository<DiscountCard, Long> {
    @EntityGraph(value = "DiscountCard.discountCardInfo", type = EntityGraph.EntityGraphType.LOAD)
    DiscountCard findById(long id);

    @EntityGraph(value = "DiscountCard.discountCardInfo", type = EntityGraph.EntityGraphType.LOAD)
    DiscountCard findByIdAndAvailableTrue(long id);

    Optional<DiscountCard> findByCardNumber(long cardNumber);

    @Query("select d.id, d.companyName, d.amountOfDiscount from DiscountCard d where lower(d.companyName) LIKE %?1%")
    Optional<List<DiscountCard>> findByCompanyName(String companyName);

    @Query("select d from Person u JOIN u.discountCards d where u.id = ?1")
    List<DiscountCard> findByUserId(long id);

    @Query("select d from DiscountCard d, Tag t " +
            "where t member of d.tags " +
            "and t.tag in ?1")
    Optional<List<DiscountCard>> findByTags(Set<String> tags);

    @Query("SELECT CASE WHEN (COUNT(cd) > 0) THEN true ELSE false END FROM DiscountCard cd WHERE cd.cardNumber = ?1 AND UPPER(cd.companyName) = ?2")
    boolean existsByNumberAndCompanyName(long number, String companyName);
}