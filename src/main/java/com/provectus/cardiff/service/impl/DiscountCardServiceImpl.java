package com.provectus.cardiff.service.impl;

import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.enums.PersonRole;
import com.provectus.cardiff.persistence.repository.DiscountCardRepository;
import com.provectus.cardiff.service.DiscountCardService;
import com.provectus.cardiff.utils.EntityUpdater;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.provectus.cardiff.utils.validator.DiscountCardValidator.validate;

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
    @Override
    public void update(DiscountCard card) {
        DiscountCard trg;
        Person person=(Person) SecurityUtils.getSubject().getPrincipal();
        if (SecurityUtils.getSubject().hasRole(PersonRole.ADMIN.name())) {
            trg = discountCardRepository.findById(card.getId());
        } else if (person.getDiscountCards().contains(discountCardRepository.findById(card.getId())))
            trg=discountCardRepository.findById((card.getId()));
        else {
            throw new AuthenticationException("Person has no permission");
        }
        validate(Optional.ofNullable(card));
        EntityUpdater.update(Optional.ofNullable(card), Optional.ofNullable(trg));
    }
    @Override
    public void delete(DiscountCard card) {
        Person person=(Person) SecurityUtils.getSubject().getPrincipal();
        if (!SecurityUtils.getSubject().hasRole(PersonRole.ADMIN.name())||person.getDiscountCards().contains(discountCardRepository.findById(card.getId())))
        {
            throw new AuthenticationException("Person has no permission");
        }
        card.setDeleted(true);
    }
    @Override
    public DiscountCard getCard(long id)
    {
        if(discountCardRepository.findById(id)==null)
            throw new RuntimeException("Discount card cannot be null");
        return discountCardRepository.findById(id);

    }

    @Override
    public DiscountCard findCardByNumber(long number)
    {
        if(discountCardRepository.findByCardNumber(number)==null)
            throw new RuntimeException("Discount card cannot be null");
        return discountCardRepository.findByCardNumber(number);
    }

    @Override
    public List<DiscountCard> findByTags(Set<String> tags) {
        if(discountCardRepository.findByTags(tags).equals(""))
            throw new RuntimeException("Discount card cannot be null");
        return  discountCardRepository.findByTags(tags);
    }

    @Override
    public List<DiscountCard> findByName(String name) {
        if(discountCardRepository.findByCompanyName(name).equals(""))
            throw new RuntimeException("Discount card cannot be null");
        return  discountCardRepository.findByCompanyName(name);
    }

    @Override
    public Page<DiscountCard> getAll(Pageable pageable) {
        SecurityUtils.getSubject().checkRole("ADMIN");
        return discountCardRepository.findAll(pageable);
    }
}
