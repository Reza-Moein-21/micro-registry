package com.gmail.rezamoeinpe.microregistrycommon.utils;

import java.nio.charset.StandardCharsets;

public class StringUtils {

    public static boolean isPureAscii(String v) {
        return StandardCharsets.US_ASCII.newEncoder().canEncode(v);
    }

    public static boolean isLetterDigitOrDash(String string) {
        if (string == null) return false;
        return string
                .chars()
                .allMatch(aChar -> Character.isLetter(aChar) || Character.isDigit(aChar) || aChar == '-');
    }
}