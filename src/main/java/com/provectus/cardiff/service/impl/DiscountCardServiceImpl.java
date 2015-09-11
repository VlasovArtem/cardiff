package com.provectus.cardiff.service.impl;

import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.enums.PersonRole;
import com.provectus.cardiff.persistence.repository.DiscountCardRepository;
import com.provectus.cardiff.persistence.repository.PersonRepository;
import com.provectus.cardiff.service.DiscountCardService;
import com.provectus.cardiff.utils.exception.EntityValidationException;
import com.provectus.cardiff.utils.validator.DiscountCardValidator;
import com.provectus.cardiff.utils.validator.TagValidator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Дмитрий on 27/08/15.
 */
@Service
@Transactional
public class DiscountCardServiceImpl implements DiscountCardService {
    @Autowired
    private DiscountCardRepository discountCardRepository;
    @Autowired
    private PersonRepository personRepository;

    @Override
    public void add (DiscountCard card) {
        if (discountCardRepository.existsByNumberAndCompanyName(card.getCardNumber(), card.getCompanyName())) {
            throw new EntityValidationException("Card with this company name and number already exist");
        }
        if(card.getExpiredDate() == null) {
            card.setExpiredDate(LocalDateTime.now().plusYears(10));
        }
        DiscountCardValidator.validate(card);
        card.getTags().stream().forEach(TagValidator::validate);
        card.setOwner(personRepository.findById((long) SecurityUtils.getSubject().getPrincipal()));
        discountCardRepository.save(card);
//        discountCardRepository.save(card);
//        Person person = personRepository.findById((long) SecurityUtils.getSubject().getPrincipal());
//        List<DiscountCard> discountCards = person.getDiscountCards();
//        discountCards.add(card);
//        person.setDiscountCards(discountCards);
    }

    @Override
    public void update (DiscountCard card) {
//        DiscountCard trg;
//        if (SecurityUtils.getSubject().hasRole(PersonRole.ADMIN.name())) {
//            trg = discountCardRepository.findById(card.getId());
//        } else if (person.getDiscountCards().contains(discountCardRepository.findById(card.getId())))
//            trg =  discountCardRepository.findById((card.getId()));
//        else {
//            throw new AuthenticationException("Person has no permission");
//        }
//        validate(card);
//        EntityUpdater.update(Optional.ofNullable(card), Optional.ofNullable(trg));
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete (DiscountCard card) {
        Person person=(Person) SecurityUtils.getSubject().getPrincipal();
        if (!SecurityUtils.getSubject().hasRole(PersonRole.ADMIN.name())||person.getDiscountCards().contains(discountCardRepository.findById(card.getId())))
        {
            throw new AuthenticationException("Person has no permission");
        }
        card.setDeleted(true);
    }
    @Override
    public DiscountCard getCard (long id) {
        return discountCardRepository.findById(id);
    }

    @Override
    public DiscountCard findAvailable(long id) {
        return discountCardRepository.findByIdAndAvailableTrue(id);
    }

    @Override
    public Optional<DiscountCard> search (long cardNumber) {
        return discountCardRepository.findByCardNumber(cardNumber);
    }

    @Override
    public Optional<List<DiscountCard>> search (Set<String> tags) {
        return discountCardRepository.findByTags(tags);
    }

    @Override
    public Optional<List<DiscountCard>> search (String name) {
        return discountCardRepository.findByCompanyName(name);
    }

    @Override
    public Page<DiscountCard> getAll (Pageable pageable) {
        return discountCardRepository.findAll(pageable);
    }
}
