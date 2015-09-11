package com.provectus.cardiff.service;

import com.provectus.cardiff.entities.Tag;

import java.util.Set;

/**
 * Created by blupashko on 26.08.2015.
 */
public interface TagService {

    Tag getTag (Long id);

    void addTag(Tag tag);

    Tag updateTag(Long id, String tag);

    void deleteTag(Long id);

    Set<Tag> findAll();
}
