package com.provectus.cardiff.utils.converter;

import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by artemvlasov on 04/09/15.
 */
public class LocalDateTimePersistenceConverterTest {
    private static LocalDateTimePersistenceConverter converter = new LocalDateTimePersistenceConverter();

    @Test
    public void convertToDatabaseColumnTest() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        assertEquals(timestamp, converter.convertToDatabaseColumn(localDateTime));
    }

    @Test
    public void convertToDatabaseColumnNullDataTest() {
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    public void convertToEntityAttributeTest() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        assertEquals(localDateTime, converter.convertToEntityAttribute(timestamp));
    }

    @Test
    public void convertToEntityAttributeNullDataTest() {
        assertNull(converter.convertToEntityAttribute(null));
    }
}
