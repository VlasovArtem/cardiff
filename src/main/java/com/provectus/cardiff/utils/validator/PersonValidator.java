package com.provectus.cardiff.utils.validator;

import com.provectus.cardiff.entities.Person;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by artemvlasov on 27/08/15.
 */
public class PersonValidator {
    /**
     * Validate <tt>Persons<tt/> entity
     * @param person <tt>Person</tt> for validation.
     * @return List<DataType> will return empty List if Person path all validation method otherwise return List of
     * DataType enum that contains error information for each invalid field
     */
    public static List<DataType> validate(Person person) {
        if(person == null) {
            throw new RuntimeException("Validate object cannot be null");
        }
        List<Data> personData = new ArrayList<>(6);
        personData.add(DataFactory.emailData(person.getEmail()));
        personData.add(DataFactory.loginData(person.getLogin()));
        personData.add(DataFactory.passwordData(person.getPassword()));
        personData.add(DataFactory.phoneNumberData(person.getPhoneNumber()));
        if(person.getDescription() != null) {
            personData.add(DataFactory.descriptionData(person.getDescription()));
        }
        if(person.getName() != null) {
            personData.add(DataFactory.nameData(person.getName()));
        }
        return validate(personData);
    }

    /**
     * Validate user data with help of Data class.
     * @param validateData List of Data class
     * @return List of DataType. It will return empty list if and only if all Data object is valid.
     */
    public static List<DataType> validate(List<Data> validateData) {
        List<DataType> invalidData = new ArrayList<>(validateData.size());
        for (Data data : validateData) {
            switch (data.getDataType()) {
                case NAME:
                    if(!validateName(data.getData())) {
                        invalidData.add(DataType.NAME);
                    }
                    break;
                case LOGIN:
                    if(!validateLogin(data.getData())) {
                        invalidData.add(DataType.LOGIN);
                    }
                    break;
                case PASSWORD:
                    if(!validatePassword(data.getData())) {
                        invalidData.add(DataType.PASSWORD);
                    }
                    break;
                case PHONE_NUMBER:
                    if(!validatePhoneNumber(data.getData())) {
                        invalidData.add(DataType.PHONE_NUMBER);
                    }
                    break;
                case DESCRIPTION:
                    if(!validateDescription(data.getData())) {
                        invalidData.add(DataType.DESCRIPTION);
                    }
                    break;
                case EMAIL:
                    if(!EmailValidator.getInstance().isValid(data.getData()))
                        invalidData.add(DataType.EMAIL);
                    break;
            }
        }
        return invalidData;
    }

    public static boolean validateName(String name) {
        String NAME_PATTERN = "^([A-Za-z]+\\s?)+[A-Za-z]$";
        return validateData(name, NAME_PATTERN) && name.length() >= 6 && name.length() <= 100;
    }

    public static boolean validateLogin(String login) {
        String LOGIN_PATTERN = "^[A-Za-z0-9_-]{6,100}$";
        return validateData(login, LOGIN_PATTERN);
    }

    public static boolean validatePassword(String password) {
        String PASSWORD_PATTERN = "^.{8,}$";
        return validateData(password, PASSWORD_PATTERN) && password.length() >= 8;
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        String PHONE_NUMBER_PATTERN = "[0-9]{9}";
        return validateData(phoneNumber, PHONE_NUMBER_PATTERN);
    }

    public static boolean validateDescription(String description) {
        String DESCRIPTION_PATTERN = ".{0,500}";
        return validateData(description, DESCRIPTION_PATTERN);
    }

    private static boolean validateData(String data, String pattern) {
        return !(data == null || pattern == null) && Pattern.compile(pattern).matcher(data).matches();
    }

    public enum DataType {
        NAME("Name should not contains any digital and length should be 6 - 100"),
        LOGIN("Login should contains next characters: a-z 0-9 _ -. And length should be 6 - 100"),
        PASSWORD("Min length of password should be 8"),
        PHONE_NUMBER("Phone number should contains"),
        DESCRIPTION("Description length should be less than 500"),
        EMAIL("Email is not matches standard pattern");
        private String error;

        DataType(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }

    public static class Data {
        private final DataType dataType;
        private final String data;


        public Data(DataType dataType, String data) {
            if(dataType == null) {
                throw new RuntimeException("Data type cannot be null");
            } else if(data == null) {
                throw new RuntimeException("Data of " + dataType.name().toLowerCase() + " cannot be null");
            }
            this.dataType = dataType;
            this.data = data;
        }

        public DataType getDataType() {
            return dataType;
        }

        public String getData() {
            return data;
        }
    }

    public static class DataFactory {
        public static Data emailData(String email) {
            return new Data(DataType.EMAIL, email);
        }
        public static Data loginData(String login) {
            return new Data(DataType.LOGIN, login);
        }
        public static Data passwordData(String password) {
            return new Data(DataType.PASSWORD, password);
        }
        public static Data phoneNumberData(long phoneNumber) {
            return new Data(DataType.PHONE_NUMBER, String.valueOf(phoneNumber));
        }
        public static Data nameData(String name) {
            return new Data(DataType.NAME, name);
        }
        public static Data descriptionData(String description) {
            return new Data(DataType.DESCRIPTION, description);
        }
    }
}
