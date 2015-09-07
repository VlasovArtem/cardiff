package com.provectus.cardiff.service;

import com.provectus.cardiff.entities.DiscountCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * Created by Дмитрий on 27/08/15.
 */
public interface DiscountCardService {
    void add(DiscountCard card);
    DiscountCard findCardByNumber(long number);
    void update(DiscountCard card);
    void delete(DiscountCard card);

    DiscountCard getCard(long id);


    List<DiscountCard> findByTags (Set<String> tags);

    List<DiscountCard> findByName (String name);

    Page<DiscountCard> getAll(Pageable pageable);

}
