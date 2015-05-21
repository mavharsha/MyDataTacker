
/*
 * Copyright (c)
 *
 *  Sree  Harsha Mamilla
 *  Pasyanthi
 *  github/mavharsha
 */

package sk.maverick.harsha.mydatatacker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Harsha on 5/7/2015.
 */
public class regexValidator {

    public static boolean validateEmail (final String incomming) {

        // Regex start with ^ and ends with $

         final String email_pattern = "^[A-Za-z0-9+]+(.[A-Za-z0-9]+)*@"
                                         +
                                        "[A-Za-z0-9]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(email_pattern);
        Matcher matcher = pattern.matcher(incomming);
        return matcher.matches();

    }

    public static boolean validateName (final String incomming) {

        // Regex start with ^ and ends with $
        final String name_pattern = "^[a-zA-z]+([ '-][a-zA-Z]+)*$";
        Pattern   pattern = Pattern.compile(name_pattern);
        Matcher matcher = pattern.matcher(incomming);
        return matcher.matches();

    }


    public static boolean validatePhoneNumber (final String incomming) {

        // Regex start with ^ and ends with $
        final String phonenumber_pattern = "^[0-9]{10}$";
        Pattern   pattern = Pattern.compile(phonenumber_pattern);
        Matcher matcher = pattern.matcher(incomming);
        return matcher.matches();
    }

    public static boolean validateNumber (final String incomming) {

        // Regex start with ^ and ends with $
        final String phonenumber_pattern = "^[0-9]*$";
        Pattern   pattern = Pattern.compile(phonenumber_pattern);
        Matcher matcher = pattern.matcher(incomming);
        return matcher.matches();
    }

    public static boolean validatePassword (final String incomming) {

        // Regex start with ^ and ends with $
        final String password_pattern = "^[a-zA-z0-9]*$";
        Pattern   pattern = Pattern.compile(password_pattern);
        Matcher matcher = pattern.matcher(incomming);
        return matcher.matches();
    }

}
