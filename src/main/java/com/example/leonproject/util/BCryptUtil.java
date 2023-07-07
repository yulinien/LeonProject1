package com.example.leonproject.util;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptUtil {

    public static String passwordEncode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());

    }

    public static Boolean passwordCheck(String userInput, String password) {
        return BCrypt.checkpw(userInput, password);
    }
}
