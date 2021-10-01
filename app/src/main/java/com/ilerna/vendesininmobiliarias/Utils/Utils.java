package com.ilerna.vendesininmobiliarias.Utils;

import java.util.regex.Pattern;

public interface Utils {

    static boolean isEmailAddressValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(email).matches();
    }
}
