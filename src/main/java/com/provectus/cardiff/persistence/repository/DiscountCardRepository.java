package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.entities.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by artemvlasov on 21/08/15.
 */
public interface DiscountCardRepository extends JpaRepository<DiscountCard, Long> {
    @EntityGraph(value = "DiscountCard.discountCardInfo", type = EntityGraph.EntityGraphType.LOAD)
    Optional<DiscountCard> findById(long id);

    @EntityGraph(value = "DiscountCard.discountCardInfo", type = EntityGraph.EntityGraphType.LOAD)
    Optional<DiscountCard> findByIdAndPickedFalse(long id);

    Optional<DiscountCard> findByCardNumber(long cardNumber);

    Page<DiscountCard> findByPickedFalse(Pageable pageable);

    @Query("select d from DiscountCard d where lower(d.companyName) LIKE %?1%")
    List<DiscountCard> findByCompanyName(String companyName);

    Page<DiscountCard> findByOwnerId(long id, Pageable pageable);

    Stream<DiscountCard> findByOwnerId(long id);

    @Query("select d from DiscountCard d, Tag t " +
            "where t member of d.tags " +
            "and t.tag in ?1")
    Optional<List<DiscountCard>> findByTags(Set<String> tags);

    @Query("SELECT DISTINCT dc FROM DiscountCard dc, Tag tag WHERE tag MEMBER OF dc.tags AND tag IN ?1")
    Page<DiscountCard> findByTags(Set<Tag> tags, Pageable pageable);

    Page<DiscountCard> findByCompanyNameIgnoreCaseContaining(String companyName, Pageable pageable);

    @Query("SELECT DISTINCT dc FROM DiscountCard dc, Tag t WHERE t MEMBER OF dc.tags AND t IN ?1 AND lower(dc.companyName) LIKE %?2%")
    Page<DiscountCard> findByTagsAndCompanyName (Set<Tag> tags, String companyName, Pageable pageable);

    @Query("SELECT CASE WHEN (COUNT(cd) > 0) THEN true ELSE false END FROM DiscountCard cd WHERE cd.cardNumber = ?1 AND UPPER(cd.companyName) = UPPER(?2)")
    boolean existsByNumberAndCompanyName(long number, String companyName);

    @Query("SELECT CASE WHEN (COUNT(cd) > 0) THEN true ELSE false END FROM DiscountCard cd " +
            "WHERE cd.id = ?1 AND cd.owner.id = ?2")
    boolean existsByDiscountCardIdAndOwnerId(long discountCardId, long ownerId);

    @Query("SELECT CASE WHEN (COUNT(cd) > 0) THEN true ELSE false END FROM DiscountCard cd WHERE cd.id = ?1 AND cd.picked = true")
    boolean isPicked (long id);

    @Query("SELECT CASE WHEN (COUNT(cd) > 0) THEN true ELSE false END FROM DiscountCard cd WHERE cd.id = ?1 AND cd.owner.id = ?2")
    boolean personDiscountCard (long discountCardId, long personId);

    @Query("SELECT dc FROM DiscountCardHistory dch, DiscountCard dc WHERE dch.discountCard.id = dc.id GROUP BY dc ORDER BY COUNT(dc) DESC")
    List<DiscountCard> findTop5DiscountCard(Pageable pageable);
}