package com.provectus.cardiff.service.impl;

import com.provectus.cardiff.entities.Tag;
import com.provectus.cardiff.entities.UnverifiedTag;
import com.provectus.cardiff.persistence.repository.TagRepository;
import com.provectus.cardiff.persistence.repository.UnverifiedTagRepository;
import com.provectus.cardiff.service.TagService;
import com.provectus.cardiff.service.UnverifiedTagService;
import com.provectus.cardiff.utils.validator.UnverifiedTagValidator;
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
public class UnverifiedTagServiceImpl implements UnverifiedTagService {
    private final static Logger LOGGER = Logger.getLogger(UnverifiedTagServiceImpl.class);

    @Autowired
    private UnverifiedTagRepository unverifiedTagRepository;
    @Autowired
    private TagRepository tagRepository;

    @Override
    public UnverifiedTag getTag(Long id) {
        return unverifiedTagRepository.findById(id);
    }

    @Override
    public List<UnverifiedTag> findAll() {
        Sort sort = new Sort(Sort.Direction.ASC, "tag");
        return unverifiedTagRepository.findAll(sort);
    }


    @Override
    public void addTag(UnverifiedTag tag) {
        UnverifiedTagValidator.validate(tag);
        unverifiedTagRepository.save(tag);
    }

    @Override
    public UnverifiedTag updateTag(Long id, String tag) {
        unverifiedTagRepository.findById(id).setTag(tag);
        return unverifiedTagRepository.findById(id);
    }

    @Override
    public void deleteTag(Long id) {
        unverifiedTagRepository.deleteById(id);
    }

    @Override
    public Page<UnverifiedTag> getAll (Pageable pageable) {
        return unverifiedTagRepository.findAll(pageable);
    }

    @Override
    public void adoptTag(long tagId)
    {
        UnverifiedTag unTag=unverifiedTagRepository.findById(tagId);
        Tag tag=new Tag();
        tag.setTag(unTag.getTag());
        tagRepository.save(tag);
        unverifiedTagRepository.delete(unTag);
    }

    @Override
    public void delete(long tagId) {
        unverifiedTagRepository.deleteById(tagId);
    }

}

