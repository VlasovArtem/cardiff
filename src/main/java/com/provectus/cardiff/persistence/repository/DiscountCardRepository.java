package com.provectus.cardiff.persistence.repository;

import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.entities.Location;
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

    @Query("SELECT d FROM DiscountCard d WHERE LOWER(d.companyName) LIKE %?1%")
    List<DiscountCard> findByCompanyName(String companyName);

    Page<DiscountCard> findByOwnerId(long id, Pageable pageable);

    Stream<DiscountCard> findByOwnerId(long id);

    @Query("SELECT d FROM DiscountCard d INNER JOIN d.tags t WHERE t.tag IN ?1")
    Optional<List<DiscountCard>> findByTags(Set<String> tags);

    @Query("SELECT dc FROM DiscountCard dc INNER JOIN dc.tags tag WHERE tag IN ?1")
    Page<DiscountCard> findByTags(Set<Tag> tags, Pageable pageable);

    Page<DiscountCard> findByCompanyNameIgnoreCaseContaining(String companyName, Pageable pageable);

    Page<DiscountCard> findByLocationId(long locationId, Pageable pageable);

    @Query("SELECT dc FROM DiscountCard dc INNER JOIN dc.tags t WHERE t IN ?1 AND lower(dc.companyName) LIKE %?2%")
    Page<DiscountCard> findByTagsAndCompanyName (Set<Tag> tags, String companyName, Pageable pageable);

    @Query("SELECT dc FROM DiscountCard dc INNER JOIN dc.tags t WHERE t IN ?1 AND dc.location.id = ?2")
    Page<DiscountCard> findByTagsAndLocationId (Set<Tag> tags, long locationId, Pageable pageable);

    @Query("SELECT dc FROM DiscountCard dc WHERE lower(dc.companyName) LIKE %?1% AND dc.location.id = ?2")
    Page<DiscountCard> findByCompanyNameAndLocationId (String companyName, long locationId, Pageable pageable);

    @Query("SELECT dc FROM DiscountCard dc INNER JOIN dc.tags t WHERE t IN ?1 AND lower(dc.companyName) LIKE %?2% AND dc.location.id = ?3")
    Page<DiscountCard> findByTagsAndCompanyNameAndLocationId (Set<Tag> tags, String companyName, long locationId, Pageable pageable);

    @Query("SELECT CASE WHEN (COUNT(cd) > 0) THEN true ELSE false END FROM DiscountCard cd WHERE cd.cardNumber = ?1 AND UPPER(cd.companyName) = UPPER(?2)")
    boolean existsByNumberAndCompanyName(long number, String companyName);

    @Query("SELECT CASE WHEN (COUNT(cd) > 0) THEN true ELSE false END FROM DiscountCard cd " +
            "WHERE cd.id = ?1 AND cd.owner.id = ?2")
    boolean existsByDiscountCardIdAndOwnerId(long discountCardId, long ownerId);

    @Query("SELECT CASE WHEN (COUNT(cd) > 0) THEN true ELSE false END FROM DiscountCard cd WHERE cd.id = ?1 AND cd.picked = true")
    boolean isPicked (long id);

    @Query("SELECT CASE WHEN (COUNT(cd) > 0) THEN true ELSE false END FROM DiscountCard cd WHERE cd.id = ?1 AND cd.owner.id = ?2")
    boolean personDiscountCard (long discountCardId, long personId);

    @Query("SELECT dc FROM DiscountCard dc WHERE dc.id IN (SELECT dch.discountCard.id FROM DiscountCardHistory dch GROUP BY dch.discountCard.id ORDER BY COUNT(*) DESC)")
    List<DiscountCard> findTop5DiscountCard (Pageable pageable);

    long countByDeletedIsFalse();
}