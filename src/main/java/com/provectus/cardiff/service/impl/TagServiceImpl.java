package com.provectus.cardiff.service.impl;

import com.provectus.cardiff.entities.Tag;
import com.provectus.cardiff.persistence.repository.TagRepository;
import com.provectus.cardiff.service.TagService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by blupashko on 26.08.2015.
 */

@Service
@Transactional
public class TagServiceImpl implements TagService {
    private final static Logger LOGGER = LogManager.getLogger(TagServiceImpl.class);

    @Qualifier("tagRepository")
    @Autowired
    TagRepository tagRepository;

    @Override
    public Tag getTag(Long id) {
        LOGGER.error("get tag");

        LOGGER.error("id=" + id);
        return tagRepository.findById(id);
    }

    @Override
    public void addTag(String tag) {
        checkTagBeforeAdding(tag);
        tagRepository.save(new Tag(tag));
    }

    @Override
    public Tag updateTag(Long id, String tag) {
        tagRepository.findById(id).setTag(tag);
        return tagRepository.findById(id);
    }

    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    private void checkTagBeforeAdding(String tag) {
        if(tag == null) {
            throw new RuntimeException("Discount card cannot be null.");
        }
        if (tag.equals("")) {
            throw new RuntimeException("Tag name is required.");
        }

        if (tagRepository.existsByTag(tag)) {
            throw new RuntimeException("This tag name is already exist.");
        }
    }
}
