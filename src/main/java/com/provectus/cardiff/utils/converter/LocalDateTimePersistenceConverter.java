package com.provectus.cardiff.utils.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by artemvlasov on 20/08/15.
 */
@Converter(autoApply = true)
public class LocalDateTimePersistenceConverter implements AttributeConverter<LocalDateTime, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime attribute) {
        if(attribute != null) {
            return Timestamp.valueOf(attribute);
        }
        return null;
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp dbData) {
        if(dbData != null) {
            return dbData.toLocalDateTime();
        }
        return null;
    }
}
