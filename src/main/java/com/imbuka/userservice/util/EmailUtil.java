package com.imbuka.userservice.util;

public class EmailUtil {
    public static String getEmailMesaage(String name, String host, String token) {
        return "Hello " + name + ",\n\nYour new Account has been created. Please click the link below to verify your account. \n\n"
                + getVerificationUrl(host, token) + "\n\nThe Support Team";
    }
    public static String getVerificationUrl(String host, String token) {
        return host + "/api/v1/users?token=" + token;
    }
}
