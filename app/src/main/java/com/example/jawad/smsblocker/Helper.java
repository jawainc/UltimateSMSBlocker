package com.example.jawad.smsblocker;

/**
 * Created by jawad on 06/05/2016.
 */
public class Helper {

    public static Boolean isBlank(String str){

        if(str.trim().equals(""))
            return true;
        else
            return false;
    }

    public static String formatToOnlyNumber(String str){
        String number = str.replaceAll("[^\\d ]", "").replaceAll("\\s+", "");
        return number;
    }

}
