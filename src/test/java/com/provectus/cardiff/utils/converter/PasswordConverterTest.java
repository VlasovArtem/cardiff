package com.provectus.cardiff.utils.converter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by artemvlasov on 04/09/15.
 */
public class PasswordConverterTest {
    public static PasswordConverter passwordConverter = new PasswordConverter();

    @Test
    public void convertToDatabaseColumnTest() {
        String test = "test";
        Byte[] stringToBytesByConverter = passwordConverter.convertToDatabaseColumn(test);
        byte[] bytes = test.getBytes();
        Byte[] stringToBytes = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            stringToBytes[i] = bytes[i];
        }
        assertEquals(stringToBytesByConverter.length, stringToBytes.length);
    }

    @Test
    public void convertToDatabaseColumnNullDataTest() {
        assertNull(passwordConverter.convertToDatabaseColumn(null));
    }

    @Test
    public void convertToEntityAttributeTest() {
        String test = "test";
        byte[] bytes = test.getBytes();
        Byte[] stringToBytes = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            stringToBytes[i] = bytes[i];
        }
        String convertedData = passwordConverter.convertToEntityAttribute(stringToBytes);
        assertEquals(test, convertedData);
    }

    @Test
    public void convertToEntityAttributeNullDataTest() {
        assertNull(passwordConverter.convertToEntityAttribute(null));
    }

}
