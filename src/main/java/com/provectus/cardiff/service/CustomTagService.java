package com.provectus.cardiff.service;

import com.provectus.cardiff.entities.CustomTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Дмитрий on 04/10/2015.
 */
public interface CustomTagService {

    void addTag(CustomTag tag);

    long countUnacceptedTags();

    List findAll();

    Page<CustomTag> getAll(Pageable pageable);

    void acceptTag(long tagId);

    void delete(long tagId);
}
