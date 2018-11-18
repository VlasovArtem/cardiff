package com.provectus.cardiff.service.impl;

import com.provectus.cardiff.entities.DiscountCard;
import com.provectus.cardiff.entities.Location;
import com.provectus.cardiff.entities.Tag;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    @Qualifier("personRepository")
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private TagRepository tagRepository;
    private final static int TOP_DISCOUNT_CARD_LIMIT = 5;

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
    public Page<DiscountCard> getAll(Set<String> tags, String companyName, Long locationId, Pageable pageable) {
        boolean tagsNotEmpty = Objects.nonNull(tags) && !tags.isEmpty();
        boolean companyNameEmpty = Objects.nonNull(companyName) && !"".equals(companyName);
        boolean locationInNotZero = Objects.nonNull(locationId) && locationId > 0;
        Set<Tag> dbTags = Objects.isNull(tags) ? null : tagRepository.findByTagIn(tags);
        if(!tagsNotEmpty && !companyNameEmpty && !locationInNotZero) {
            return discountCardRepository.findAll(pageable);
        } else if(!companyNameEmpty && !locationInNotZero) {
            return discountCardRepository.findByTags(dbTags, pageable);
        } else if(!tagsNotEmpty && !locationInNotZero) {
            return discountCardRepository.findByCompanyNameIgnoreCaseContaining(companyName, pageable);
        } else if(!tagsNotEmpty && !companyNameEmpty) {
            return discountCardRepository.findByLocationId(locationId, pageable);
        } else if(tagsNotEmpty && companyNameEmpty && !locationInNotZero) {
            return discountCardRepository.findByTagsAndCompanyName(dbTags, companyName, pageable);
        } else if(!tagsNotEmpty) {
            return discountCardRepository.findByCompanyNameAndLocationId(companyName, locationId, pageable);
        } else if(!companyNameEmpty) {
            return discountCardRepository.findByTagsAndLocationId(dbTags, locationId, pageable);
        } else {
            return discountCardRepository.findByTagsAndCompanyNameAndLocationId(dbTags, companyName, locationId, pageable);
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

    @Override
    public long count() {
        return discountCardRepository.countByDeletedIsFalse();
    }

    @Override
    public List<DiscountCard> top5() {
        return discountCardRepository.findTop5DiscountCard(new PageRequest(0, TOP_DISCOUNT_CARD_LIMIT));
    }
}
