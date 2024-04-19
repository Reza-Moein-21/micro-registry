package com.gmail.rezamoeinpe.microregistrycommon.utils;

public class ArrayUtils {

    public static byte[] joinByteArrays(byte[] array1, byte[] array2, byte[] array3) {

        byte[] result = new byte[array1.length + array2.length + array3.length];
        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        System.arraycopy(array3, 0, result, array1.length + array2.length, array3.length);

        return result;
    }
}
