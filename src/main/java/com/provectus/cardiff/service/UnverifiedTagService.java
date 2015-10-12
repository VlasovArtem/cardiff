package com.provectus.cardiff.service;

import com.provectus.cardiff.entities.UnverifiedTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Дмитрий on 04/10/2015.
 */
public interface UnverifiedTagService {

    UnverifiedTag getTag(Long id);

    void addTag(UnverifiedTag tag);

    UnverifiedTag updateTag(Long id, String tag);

    void deleteTag(Long id);

    List findAll();
    Page<UnverifiedTag> getAll(Pageable pageable);
    void adoptTag(long tagId);
    void delete(long tagId);
}
