package com.provectus.cardiff.service.impl;

import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.persistence.repository.DiscountCardRepository;
import com.provectus.cardiff.persistence.repository.PersonRepository;
import com.provectus.cardiff.service.DiscountCardService;
import com.provectus.cardiff.utils.exception.DataUniqueException;
import com.provectus.cardiff.utils.exception.EntityValidationException;
import com.provectus.cardiff.utils.security.AuthenticatedPersonPrincipalUtil;
import com.provectus.cardiff.utils.validator.DiscountCardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new EntityValidationException("Card with this company name or number already exist");
        }
        DiscountCardValidator.validate(card);
        card.setOwner(personRepository.findById(AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal().get().getId()));
        discountCardRepository.save(card);
    }

    @Override
    public void update (DiscountCard card) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete (long cardId) {
        discountCardRepository.findById(cardId).ifPresent(d -> d.setDeleted(true));
    }

    @Override
    public Optional<DiscountCard> getCard (long id) {
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
        return discountCardRepository.findByAvailableTrue(pageable);
    }

    @Override
    public Page<DiscountCard> getAuthenticatedPersonDiscountCards(Pageable pageable) {
        return discountCardRepository.findByOwnerId(AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal().get
                        ().getId(), pageable);
    }

    @Override
    public void removeOwnerCards(long ownerId) {
        discountCardRepository.findByOwnerId(ownerId).forEach(d -> d.setDeleted(true));
    }

    @Override
    public void restoreOwnerCard(long ownerId) {
        discountCardRepository.findByOwnerId(ownerId).forEach(d -> d.setDeleted(false));
    }

    @Override
    public boolean checkDiscountCardIsUnique(long discountCardNumber, String companyName) {
        if(discountCardRepository.existsByNumberAndCompanyName(discountCardNumber, companyName)) {
            throw new DataUniqueException("Discount card with entered number and company name is already exists");
        }
        return true;
    }
}
