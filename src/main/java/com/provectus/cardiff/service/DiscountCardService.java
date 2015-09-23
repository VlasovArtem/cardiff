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

    Optional<DiscountCard> search (long cardNumber);

    void update (DiscountCard card);

    void delete (long cardId);

    Optional<DiscountCard> getCard (long id);

    Optional<List<DiscountCard>> search (Set<String> tags);

    Optional<List<DiscountCard>> search (String name);

    Page<DiscountCard> getAll (Pageable pageable);

    Page<DiscountCard> getAuthenticatedPersonDiscountCards(Pageable pageable);

    void removeOwnerCards(long ownerId);

    void restoreOwnerCard(long ownerId);

    boolean checkDiscountCardIsUnique(long discountCardNumber, String companyName);
}
