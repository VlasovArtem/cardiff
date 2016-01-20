package com.provectus.cardiff.service;

import com.provectus.cardiff.entities.DiscountCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Дмитрий on 27/08/15.
 */
public interface DiscountCardService {
    void add (DiscountCard card);

    Optional<DiscountCard> searchByCardNumber (long cardNumber);

    void update (DiscountCard card);

    void delete (long cardId);

    Optional<DiscountCard> getCard (long id);

    Optional<List<DiscountCard>> searchByTags (Set<String> tags);

    Page<DiscountCard> getAll (Pageable pageable);

    Page<DiscountCard> getAll (Set<String> tags, String companyName, Pageable pageable);

    Page<DiscountCard> getAuthenticatedPersonDiscountCards (Pageable pageable);

    List<DiscountCard> searchByCompanyName (String companyName);

    List<DiscountCard> findAll ();

    void removeOwnerCards (long ownerId);

    void restoreOwnerCard (long ownerId);

    boolean checkDiscountCardIsUnique (long discountCardNumber, String companyName);

    boolean authPersonDiscountCard (long discountCardId);

    long count();

    List<DiscountCard> top5 ();
}
