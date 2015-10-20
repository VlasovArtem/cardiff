package com.provectus.cardiff.service;

import com.provectus.cardiff.entities.Tag;

import java.util.List;

/**
 * Created by blupashko on 26.08.2015.
 */
public interface TagService {

    Tag getTag (long id);

    void addTag(Tag tag);

    void updateTag(long id, String tag);

    void deleteTag(long id);

    List findAll();
}
