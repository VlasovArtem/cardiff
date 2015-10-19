package com.provectus.cardiff.service.impl;

import com.provectus.cardiff.entities.Tag;
import com.provectus.cardiff.persistence.repository.TagRepository;
import com.provectus.cardiff.service.TagService;
import com.provectus.cardiff.utils.validator.TagValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by blupashko on 26.08.2015.
 */

@Service
@Transactional
public class TagServiceImpl implements TagService {
    private final static Logger LOGGER = Logger.getLogger(TagServiceImpl.class);

    @Autowired
    private TagRepository tagRepository;

    @Override
    public Tag getTag(Long id) {
        return tagRepository.findOne(id);
    }

    @Override
    public List<Tag> findAll() {
        Sort sort = new Sort(Sort.Direction.ASC, "tag");
        return tagRepository.findAll(sort);
    }


    @Override
    public void addTag(Tag tag) {
        TagValidator.validate(tag);
        tagRepository.save(tag);
    }

    @Override
    public void updateTag(Long id, String tag) {
        tagRepository.findOne(id).setTag(tag);
    }

    @Override
    public void deleteTag(Long id) {
        tagRepository.delete(id);
    }
}
