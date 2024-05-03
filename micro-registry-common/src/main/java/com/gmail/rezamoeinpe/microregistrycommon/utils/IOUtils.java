package com.gmail.rezamoeinpe.microregistrycommon.utils;

import java.io.ByteArrayInputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class IOUtils {
    public static ReadableByteChannel toChannel(String text) {
        return Channels.newChannel(new ByteArrayInputStream(text.getBytes()));
    }

    public static ReadableByteChannel toChannel(byte[] bytes) {
        return Channels.newChannel(new ByteArrayInputStream(bytes));
    }
}
