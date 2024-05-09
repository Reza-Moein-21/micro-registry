package com.gmail.rezamoeinpe.microregistrycommon.utils;

import com.gmail.rezamoeinpe.microregistrycommon.exception.ServerErrorException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class IOUtils {
    private static final int BUFFER_SIZE = 1024;

    public static ReadableByteChannel toChannel(String text) {
        return Channels.newChannel(new ByteArrayInputStream(text.getBytes()));
    }

    public static ReadableByteChannel toChannel(byte[] bytes) {
        return Channels.newChannel(new ByteArrayInputStream(bytes));
    }

    public static ByteBuffer readToBuffer(ReadableByteChannel channel) {
        var buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        try {
            channel.read(buffer);
        } catch (IOException e) {
            throw new ServerErrorException(e.getMessage());
        }
        buffer.flip();
        return buffer;
    }
}
