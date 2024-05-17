package com.example.recyclepro.Validation;

import java.util.regex.Pattern;

public class Regex {
    public static boolean checkEmail(String email) {
        // Kiểm tra theo cú pháp email
        String emailRegex = "^(?:[a-zA-Z0-9_\\.-]+)@(?:([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,})$";

        return Pattern.matches(emailRegex, email);
    }

    public static boolean checkPass(String password) {
        String passwordRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$";
        return password.length() >= 6;
        // return Pattern.matches(passwordRegex, password);
    }

    public static boolean checkUserName(String userName) {
        String userNameRegex = "^[A-ZÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐ][a-zàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ]*(?:[ ][A-ZÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐ][a-zàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ]*)*$";
        return Pattern.matches(userNameRegex, userName);
    }

    public static  boolean checkPhoneNumber(String phoneNumber){
        String phoneNumberRegex = "^0\\d{9}$";
        return Pattern.matches(phoneNumberRegex, phoneNumber);
    }

}
