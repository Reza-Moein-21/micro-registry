package com.gmail.rezamoeinpe.microregistrycommon.utils;

import java.nio.charset.StandardCharsets;

public class StringUtils {

    public static boolean isNotPureAscii(String v) {
        return !StandardCharsets.US_ASCII.newEncoder().canEncode(v);
    }

    public static boolean isLetterDigitOrDash(String string) {
        if (string == null) return false;
        return string
                .chars()
                .allMatch(aChar -> isLetterDigitOrDash((char) aChar));
    }

    public static boolean isLetterDigitOrDash(Character aChar) {
        if (aChar == null) return false;
        return Character.isLetter(aChar) || Character.isDigit(aChar) || aChar == '-';
    }
}