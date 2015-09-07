package com.provectus.cardiff.service;

import com.provectus.cardiff.entities.DiscountCard;

import java.util.List;
import java.util.Set;

/**
 * Created by Дмитрий on 27/08/15.
 */
public interface DiscountCardService {
    void add(DiscountCard card);
    DiscountCard findCardByNumber(long number);

    List<DiscountCard> findByTags (Set<String> tags);

}
