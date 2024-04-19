package com.gmail.rezamoeinpe.microregistryservice.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

public final class HeartBeatParser {
    private final static byte MAX_SERVICE_NAME_LENGTH = 30;
    private final static byte MIN_SERVICE_NAME_LENGTH = 2;
    private final static char START_CHARACTER = '[';
    private final static char END_CHARACTER = ']';


    public static String parser(ReadableByteChannel channel) throws HeartBeatException {
        if (channel == null) throw new HeartBeatException(HeartBeatParserError.SERVER_ERROR);

        var buffer = ByteBuffer.allocateDirect(1024);

        try {
            channel.read(buffer);
        } catch (IOException e) {
            throw new HeartBeatException(HeartBeatParserError.SERVER_ERROR, e.getMessage());
        }

        buffer.flip();
        StringBuilder builder = new StringBuilder();
        boolean startCharFound = false;
        boolean endCharFound = false;
        int index = 0;
        while (buffer.hasRemaining()) {
            if (index + 1 > MAX_SERVICE_NAME_LENGTH)
                throw new HeartBeatException(HeartBeatParserError.MAX_SERVICE_NAME);

            var nextByte = buffer.get();

            if (startCharFound) {
                if (index == 1 && !Character.isLetter(nextByte))
                    throw new HeartBeatException(HeartBeatParserError.INVALID_SERVICE_NAME_BEGINNING);

                if (nextByte == END_CHARACTER) {
                    endCharFound = true;
                    break;
                }

                if (!isLetterDigitOrDash(nextByte))
                    throw new HeartBeatException(HeartBeatParserError.INVALID_SERVICE_NAME);

                builder.append((char) nextByte);

            } else if (nextByte == START_CHARACTER) {
                startCharFound = true;
            } else {
                throw new HeartBeatException(HeartBeatParserError.INVALID_START_TOKEN);
            }


            index++;
        }
        if (!startCharFound)
            throw new HeartBeatException(HeartBeatParserError.INVALID_START_TOKEN);

        if (!endCharFound)
            throw new HeartBeatException(HeartBeatParserError.INVALID_END_TOKEN);

        if (builder.length() < MIN_SERVICE_NAME_LENGTH)
            throw new HeartBeatException(HeartBeatParserError.MIN_SERVICE_NAME);


        return builder.toString();
    }

    private static boolean isLetterDigitOrDash(byte nextByte) {
        return Character.isLetter(nextByte) || Character.isDigit(nextByte) || nextByte == '-';
    }
}
