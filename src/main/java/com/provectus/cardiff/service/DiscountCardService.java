package com.provectus.cardiff.service;

import com.provectus.cardiff.entities.DiscountCard;

/**
 * Created by Дмитрий on 27/08/15.
 */
public interface DiscountCardService {
    void add(DiscountCard card);
    DiscountCard findCardByNumber(long number);
}
