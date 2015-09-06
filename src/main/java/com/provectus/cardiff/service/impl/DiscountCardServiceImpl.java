package com.provectus.cardiff.service.impl;

import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.persistence.repository.DiscountCardRepository;
import com.provectus.cardiff.service.DiscountCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Дмитрий on 27/08/15.
 */
@Service
@Transactional
public class DiscountCardServiceImpl implements DiscountCardService {
    @Autowired
    DiscountCardRepository discountCardRepository;

    @Override
    public void add(DiscountCard card) {
        checkCardBeforeAdding(card);
        discountCardRepository.save(card);
    }
    private void checkCardBeforeAdding(DiscountCard discount_card) {
        if(discount_card == null) {
            throw new RuntimeException("Discount card cannot be null");
        }
        List<String> requiredData = new ArrayList<>(4);
        if (discount_card.getCardNumber() == 0) {
            requiredData.add("Card number");
        }
        if (discount_card.getExpiredDate() == null) {
            requiredData.add("Expiration date");
        }
        if (discount_card.getCompanyName() == null) {
            requiredData.add("Company name");
        }
        if (discount_card.getAmountOfDiscount() == 0) {
            requiredData.add("Amount of discount");
        }
        if (requiredData.size() != 0) {
            throw new RuntimeException("Next person data is required: " + requiredData.stream().collect(Collectors
                    .joining(", ")));
        }
        if (discountCardRepository.existsByNumberAndCompanyName(discount_card.getCardNumber(), discount_card.getCompanyName())) {
            throw new RuntimeException("Card with this company name and number already exist");
        }
    }
    public DiscountCard findCardByNumber(long number)
    {
        if(discountCardRepository.findById(number)==null)
            throw new RuntimeException("Discount card cannot be null");

        return discountCardRepository.findById(number);
    }
}
