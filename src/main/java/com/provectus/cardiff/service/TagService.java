package com.provectus.cardiff.service;

import com.provectus.cardiff.entities.Tag;

/**
 * Created by blupashko on 26.08.2015.
 */
public interface TagService {
 //   Tag[] getTags ();
    Tag getTag (Long id);
    void addTag(String tag);
    Tag updateTag(Long id, String tag);
    void deleteTag(Long id);

}
