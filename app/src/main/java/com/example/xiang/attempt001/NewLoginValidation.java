package com.example.xiang.attempt001;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by xiang on 2017-06-18.
 */
public class NewLoginValidation {

    public static boolean validateFields(String name){

        if (TextUtils.isEmpty(name)) {

            return false;

        } else {

            return true;
        }
    }

    public static boolean validateEmail(String string) {

        if (TextUtils.isEmpty(string) || !Patterns.EMAIL_ADDRESS.matcher(string).matches()) {

            return false;

        } else {

            return  true;
        }
    }
}