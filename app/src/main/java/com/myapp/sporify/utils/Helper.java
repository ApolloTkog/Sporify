package com.myapp.sporify.utils;

public final class Helper {


    /**
     *
     * Method that converts seconds to duration in a string value
     *
     * @param seconds time in seconds
     * @return String value in the form of a duration "MM:SS" -> "02:01"
     */
    public static String durationConverter(int seconds) {
        int remainder = seconds;

        int mins = seconds / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

//        return "0" + mins + ":" + (String.valueOf(secs).length() <= 1 ?  "0" + secs : String.valueOf(secs));
        return (String.valueOf(mins).length() <= 1 ?  "0" + mins : mins) + ":" + (String.valueOf(secs).length() <= 1 ?  "0" + secs : String.valueOf(secs));
    }
}
