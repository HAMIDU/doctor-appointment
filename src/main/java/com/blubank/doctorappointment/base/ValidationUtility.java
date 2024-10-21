package com.blubank.doctorappointment.base;

import java.util.regex.Pattern;


public class ValidationUtility {
    final static Short MINIMUM_PASSWORD_LENGTH=4;

    public static boolean validatePassword(String password) {
        return Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&^()_~;]{" +
                MINIMUM_PASSWORD_LENGTH + ",}$").matcher(password).matches();
    }

    private ValidationUtility() {
    }
}
