package com.provectus.cardiff.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by artemvlasov on 27/08/15.
 */
public class EntityUpdater {
    /**
     * Update two equals type data before update in database
     * @param src Source Object
     * @param trg Target Object
     * @param <T> Entity object type
     */
    public static <T> void update(Optional<T> src, Optional<T> trg) {
        if(src.isPresent() && trg.isPresent()) {
            if(src.get().getClass().isAssignableFrom(trg.get().getClass())) {
                updateEntity(src.get(), trg.get());
            } else {
                throw new IllegalArgumentException("Objects has different types");
            }
        }
    }

    private static <T> void updateEntity(T source, T target) {
        List<String> updatedFields = getUpdatedFields(target);
        if(updatedFields != null) {
            BeanWrapper trg = new BeanWrapperImpl(target);
            BeanWrapper src = new BeanWrapperImpl(source);
            for (PropertyDescriptor descriptor : BeanUtils.getPropertyDescriptors(source.getClass())) {
                String propName = descriptor.getName();
                if(updatedFields.contains(propName)) {
                    if(trg.getPropertyValue(propName) != src.getPropertyValue(propName)) {
                        trg.setPropertyValue(propName, src.getPropertyValue(propName));
                    }
                }
            }
        }
    }

    /**
     * Return updated fields for each entity
     * @param updatedObject Entity object
     * @return List of updated fields if object class name matches with switch case.
     */
    private static List<String> getUpdatedFields(Object updatedObject) {
        String className = updatedObject.getClass().getSimpleName();
        List<String> updatedFields = null;
        switch(className) {
            case "Person":
                updatedFields = Arrays.asList("name", "login", "email", "phoneNumber", "description");
                break;
        }
        return updatedFields;
    }
}
