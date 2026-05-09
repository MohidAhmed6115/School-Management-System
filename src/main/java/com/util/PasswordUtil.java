package com.util;

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

    public class CipherUtil {

        private static final int[] baseKey = {
                13, 22, 1, 29, 30, 14, 22, 31, 20, 22,
                29, 22, 17, 30, 14, 4, 28, 26, 2, 23,
                30, 2, 29, 11, 17, 24, 10, 17, 22, 12,
                13, 15, 6, 29, 21, 8, 11, 0, 17, 29,
                16, 6, 5, 19, 22, 25, 29, 23, 8, 7,
                1, 0, 17, 3, 9, 20, 13, 8, 19, 13,
                29, 31, 19, 29, 31, 3, 10, 16, 30, 3,
                26, 18, 5, 8, 24, 17, 26, 23, 3, 3,
                9, 25, 21, 10, 29, 25, 24, 23, 22, 14,
                26, 14, 24, 26, 4, 9, 1, 18, 7, 31};

        public static String encode(String text) {

            // Convert String → char array because Strings are immutable in Java
            char[] chars = text.toCharArray();

            // Loop through every character in the input string
            for (int i = 0; i < chars.length; i++) {
                // Pick a key from baseKey array
                int key = baseKey[i % baseKey.length];

                /*
                 * the following compares each binary bit of ascii code of the character with key+i
                 */
                chars[i] = (char) (chars[i] ^ (key + i));
            }

            // Convert char array back to String
            return new String(chars);
        }

        public static String decode(String text) {

            char[] chars = text.toCharArray();

            for (int i = 0; i < chars.length; i++) {

                int key = baseKey[i % baseKey.length];

                /*
                 * (A ^ B) ^ B = A
                 * so encrypted text becomes same
                 */
                chars[i] = (char) (chars[i] ^ (key + i));
            }

            return new String(chars);
        }
    }

}