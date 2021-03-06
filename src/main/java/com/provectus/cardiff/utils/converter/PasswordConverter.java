package com.provectus.cardiff.utils.converter;

import javax.persistence.AttributeConverter;

/**
 * Created by artemvlasov on 23/08/15.
 */
public class PasswordConverter implements AttributeConverter<String, Byte[]> {
    @Override
    public Byte[] convertToDatabaseColumn(String attribute) {
        if(attribute != null) {
            byte[] password = attribute.getBytes();
            Byte[] bytePassword = new Byte[password.length];
            for (int i = 0; i < password.length; i++) {
                bytePassword[i] = password[i];
            }
            return bytePassword;
        }
        return null;
    }

    @Override
    public String convertToEntityAttribute(Byte[] dbData) {
        if(dbData != null) {
            byte[] password = new byte[dbData.length];
            for (int i = 0; i < dbData.length; i++) {
                password[i] = dbData[i];
            }
            return new String(password);
        }
        return null;
    }
}
