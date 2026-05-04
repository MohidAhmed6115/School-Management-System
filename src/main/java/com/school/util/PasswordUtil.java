package com.school.util;

import com.school.model.User;

public class PasswordUtil {
    public static <T extends User> boolean checkCurrentPassword(T user, String enteredPassword) {
        if (user.getPassword().equals(enteredPassword)) {
            return true;
        }
        return false;
    }

    public static boolean checkNewPassword(String enteredNewPassword) {
        boolean capital = false;
        boolean lower = false;
        boolean number = false;
        boolean symbol = false;

        for (int i = 0; i < enteredNewPassword.length(); i++) {

            char ch = enteredNewPassword.charAt(i);

            if (Character.isUpperCase(ch)) capital = true;

            if (Character.isLowerCase(ch)) lower = true;

            if (Character.isDigit(ch)) number = true;

            if (!Character.isLetterOrDigit(ch)) symbol = true;

            if (capital && lower && number && symbol) return true;
        }

        return false;

    }

    public static boolean checkConfirmPassword(String enteredNewPassword, String enteredConfirmNewPassword) {
        if (enteredNewPassword.equals(enteredConfirmNewPassword)) {
            return true;
        }
        return false;
    }
}