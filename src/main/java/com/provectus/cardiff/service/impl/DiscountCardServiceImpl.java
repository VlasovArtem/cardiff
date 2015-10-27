package com.provectus.cardiff.service.impl;

import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.persistence.repository.DiscountCardRepository;
import com.provectus.cardiff.persistence.repository.PersonRepository;
import com.provectus.cardiff.persistence.repository.TagRepository;
import com.provectus.cardiff.service.DiscountCardService;
import com.provectus.cardiff.utils.EntityUpdater;
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
import java.util.Objects;
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
    @Autowired
    private TagRepository tagRepository;

    @Override
    public void add (DiscountCard card) {
        if (discountCardRepository.existsByNumberAndCompanyName(card.getCardNumber(), card.getCompanyName())) {
            throw new EntityValidationException("Card with this company companyName or number already exist");
        }
        DiscountCardValidator.validate(card);
        card.setOwner(personRepository.findById(AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal().get().getId()));
        discountCardRepository.save(card);
    }

    @Override
    public void update (DiscountCard card) {
        Optional<DiscountCard> updatedCard = discountCardRepository.findById(card.getId());
        if(!updatedCard.isPresent()) {
            throw new EntityValidationException("Card with using discount card id is not exists");
        } else if (discountCardRepository.existsByNumberAndCompanyName(card.getCardNumber(), card.getCompanyName())) {
            if(!discountCardRepository.existsByDiscountCardIdAndOwnerId(card.getId(), AuthenticatedPersonPrincipalUtil
                    .getAuthenticationPrincipal().get().getId())) {
                throw new EntityValidationException("Card with this company companyName or number already exist");
            }
        }
        DiscountCardValidator.validate(card);
        EntityUpdater.update(Optional.of(card), updatedCard);
    }

    @Override
    public void delete (long cardId) {
        discountCardRepository.findById(cardId).ifPresent(d -> d.setDeleted(true));
    }

    @Override
    public Optional<DiscountCard> getCard (long id) {
        return discountCardRepository.findById(id);
    }

    @Override
    public List<DiscountCard> findAll() {
        return discountCardRepository.findAll();
    }

    @Override
    public Optional<List<DiscountCard>> searchByTags (Set<String> tags) {
        return discountCardRepository.findByTags(tags);
    }

    @Override
    public List<DiscountCard> searchByCompanyName (String companyName) {
        return discountCardRepository.findByCompanyName(companyName.toLowerCase());
    }

    @Override
    public Optional<DiscountCard> searchByCardNumber (long cardNumber) {
        return discountCardRepository.findByCardNumber (cardNumber);
    }

    @Override
    public Page<DiscountCard> getAll (Pageable pageable) {
        return discountCardRepository.findAll(pageable);
    }

    @Override
    public Page<DiscountCard> getAll (Set<String> tags, Pageable pageable) {
        return discountCardRepository.findByTags(tagRepository.findByTagIn(tags), pageable);
    }

    @Override
    public Page<DiscountCard> getAll(Set<String> tags, String companyName, Pageable pageable) {
        if(Objects.isNull(companyName)) {
            return discountCardRepository.findByTags(tagRepository.findByTagIn(tags), pageable);
        } else if (Objects.isNull(tags)) {
            return discountCardRepository.findByCompanyNameIgnoreCaseContaining(companyName.toLowerCase(), pageable);
        } else {
            return discountCardRepository.findByTagsAndCompanyName(tagRepository.findByTagIn(tags), companyName.toLowerCase(),
                    pageable);
        }
    }

    @Override
    public Page<DiscountCard> getAuthenticatedPersonDiscountCards(Pageable pageable) {
        return discountCardRepository.findByOwnerId(AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal().get
                        ().getId(), pageable);
    }


    @Override
    public void removeOwnerCards (long ownerId) {
        discountCardRepository.findByOwnerId(ownerId).forEach(d -> {
            if(!d.isPicked()) {
                d.setDeleted(true);
            }
        });
    }

    @Override
    public void restoreOwnerCard (long ownerId) {
        discountCardRepository.findByOwnerId(ownerId).forEach(d -> d.setDeleted(false));
    }

    @Override
    public boolean checkDiscountCardIsUnique(long discountCardNumber, String companyName) {
        if(discountCardRepository.existsByNumberAndCompanyName(discountCardNumber, companyName)) {
            throw new DataUniqueException("Discount card with entered number and company companyName is already exists");
        }
        return true;
    }

    @Override
    public boolean authPersonDiscountCard(long discountCardId) {
        return discountCardRepository.personDiscountCard(discountCardId, AuthenticatedPersonPrincipalUtil
                .getAuthenticationPrincipal().get().getId());
    }
}
