package com.provectus.cardiff.service.impl;

import com.provectus.cardiff.entities.Person;
import com.provectus.cardiff.entities.Tag;
import com.provectus.cardiff.entities.CustomTag;
import com.provectus.cardiff.persistence.repository.PersonRepository;
import com.provectus.cardiff.persistence.repository.TagRepository;
import com.provectus.cardiff.persistence.repository.CustomTagRepository;
import com.provectus.cardiff.service.CustomTagService;
import com.provectus.cardiff.utils.security.AuthenticatedPersonPrincipalUtil;
import com.provectus.cardiff.utils.validator.CustomTagValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Дмитрий on 04/10/2015.
 */

@Service
@Transactional
public class CustomTagServiceImpl implements CustomTagService {
    private final static Logger LOGGER = Logger.getLogger(CustomTagServiceImpl.class);
    private final static int CUSTOM_TAG_LIMIT = 5;

    @Autowired
    private CustomTagRepository customTagRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PersonRepository personRepository;

    @Override
    public List<CustomTag> findAll() {
        Sort sort = new Sort(Sort.Direction.ASC, "tag");
        return customTagRepository.findAll(sort);
    }


    @Override
    public void addTag(CustomTag tag) {
        tag.setTag(tag.getTag().toLowerCase());
        if(customTagRepository.existsByTag(tag.getTag())) {
            throw new IllegalArgumentException("Custom tag is already exists.");
        } else if(customTagRepository.countByAuthorId(AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal()
                .get().getId()) > CUSTOM_TAG_LIMIT) {
            throw new RuntimeException("Limit of custom tag is exceeded. Limit for custom tags is equals to 5.");
        } else if(tagRepository.existsByTag(tag.getTag())) {
            throw new IllegalArgumentException("Tag is already exists in database");
        }
        CustomTagValidator.validate(tag);
        Person author = new Person();
        author.setId(AuthenticatedPersonPrincipalUtil.getAuthenticationPrincipal().get().getId());
        tag.setAuthor(author);
        customTagRepository.save(tag);
    }

    @Override
    public long countUnacceptedTags() {
        return customTagRepository.count();
    }

    @Override
    public Page<CustomTag> getAll (Pageable pageable) {
        return customTagRepository.findAll(pageable);
    }

    @Override
    public void acceptTag(long tagId) {
        CustomTag customTag = customTagRepository.findById(tagId);
        if(customTag == null) {
            throw new NullPointerException("Custom tag is not exists with the specified id");
        }
        Tag tag = new Tag();
        tag.setTag(customTag.getTag());
        tagRepository.save(tag);
        customTagRepository.delete(customTag);
    }

    @Override
    public void delete(long tagId) {
        customTagRepository.deleteById(tagId);
    }

}

