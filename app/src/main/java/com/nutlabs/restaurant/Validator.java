package com.nutlabs.restaurant;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mdimran on 4/11/2016.
 */
public class Validator {

    public static boolean validateName(final String name){
        Pattern pattern = Pattern.compile(new String("^[a-zA-Z\\s]*$"));
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
    public static boolean validateEmail(final String emailText) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches();
    }

    public static boolean validatePhone(final String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

}


