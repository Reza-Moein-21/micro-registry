package com.gmail.rezamoeinpe.microregistrycommon.protocol.heartbeat;

import com.gmail.rezamoeinpe.microregistrycommon.exception.HeartBeatRequestParserException;
import com.gmail.rezamoeinpe.microregistrycommon.exception.HeartBeatRequestParserException.HeartBeatRequestParserError;
import com.gmail.rezamoeinpe.microregistrycommon.exception.ServiceException;
import com.gmail.rezamoeinpe.microregistrycommon.protocol.Parser;
import com.gmail.rezamoeinpe.microregistrycommon.protocol.ServiceModel;
import com.gmail.rezamoeinpe.microregistrycommon.utils.StringUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

import static com.gmail.rezamoeinpe.microregistrycommon.exception.HeartBeatRequestParserException.HeartBeatRequestParserError.*;

public final class HeartBeatRequestParser implements Parser<ServiceModel> {

    private static final int BUFFER_SIZE = 1024;

    private static final byte CLOSING_BRACKET = 0x5D;

    private static final byte OPENING_BRACE = 0x7B;
    private static final byte CLOSING_BRACE = 0x7D;

    private static final byte COMMA = 0x2C;

    public final static byte MAX_SERVICE_NAME_LENGTH = 30;
    public final static byte MIN_SERVICE_NAME_LENGTH = 2;

    public final static byte MAX_HOST_NAME_LENGTH = 30;
    public final static byte MIN_HOST_NAME_LENGTH = 2;

    public final static int MAX_PORT_NUMBER = 65535;// 2^16-1
    public final static byte MIN_PORT_NUMBER = 1;
    public final static byte MAX_ENTRY = 100;


    //            HeartBeat
    private static final byte[] START_TOKENS = new byte[]{
            0x48,//H
            0x65,//e
            0x61,//a
            0x72,//r
            0x74,//t
            0x42,//B
            0x65,//e
            0x61,//a
            0x74,//t
            0x5B//[
    };

    private String serviceName;
    private String host;
    private int port;
    private String entry;

    private final StringBuilder sb = new StringBuilder();
    private int startTokenCounter = 0;
    private boolean startTokenFound;
    private boolean endTokenFound;
    private int numberOfServiceName = 0;
    private int numberOfHost = 0;
    private int numberOfPort = 0;
    private int numberOfEntry = 0;
    private boolean modelBeginningFound;

    @Override
    public ServiceModel parser(ReadableByteChannel channel) throws ServiceException {
        if (channel == null)
            throw new HeartBeatRequestParserException(SERVER_ERROR);

        var buffer = readToBuffer(channel);

        while (buffer.hasRemaining()) {
            byte nextByte = buffer.get();

            if (!startTokenFound) {
                this.startTokenFound = startTokenFound(nextByte);
                continue;
            }

            if (!modelBeginningFound) {
                modelBeginningFound = modelBeginningFound(nextByte);
                continue;
            }

            if (this.serviceName == null) {
                this.serviceName = serviceNameFound(nextByte);
                continue;
            }

            if (this.host == null) {
                this.host = hostFound(nextByte);
                continue;
            }

            if (this.port == 0) {
                this.port = portFound(nextByte);
                continue;
            }

            if (this.entry == null) {
                this.entry = entryFound(nextByte);
                continue;
            }

            this.endTokenFound = nextByte == CLOSING_BRACKET;
        }

        if (!this.startTokenFound)
            throw new HeartBeatRequestParserException(INVALID_START_TOKEN);

        if (!this.endTokenFound)
            throw new HeartBeatRequestParserException(INVALID_END_TOKEN);

        return new ServiceModel(this.serviceName, this.host, this.port, this.entry);
    }


    private void clearStringBuffer() {
        this.sb.delete(0, sb.length());
    }

    private String serviceNameFound(byte nextByte) {
        if (this.numberOfServiceName == 0) clearStringBuffer();

        if (this.numberOfServiceName > MAX_SERVICE_NAME_LENGTH) {
            if (nextByte == COMMA)
                return sb.toString();
            throw new HeartBeatRequestParserException(MAX_SERVICE_NAME);
        }

        if (nextByte == COMMA) {
            if (numberOfServiceName < MIN_SERVICE_NAME_LENGTH)
                throw new HeartBeatRequestParserException(MIN_SERVICE_NAME);
            return sb.toString();
        }

        if (this.numberOfServiceName == 0 && !Character.isLetter((char) nextByte))
            throw new HeartBeatRequestParserException(INVALID_SERVICE_NAME_BEGINNING);


        if (!StringUtils.isLetterDigitOrDash((char) nextByte))
            throw new HeartBeatRequestParserException(INVALID_SERVICE_NAME);

        this.numberOfServiceName++;
        sb.append((char) nextByte);
        return null;
    }

    private Integer portFound(byte nextByte) {
        if (numberOfPort == 0) clearStringBuffer();

        if (nextByte == COMMA || nextByte == CLOSING_BRACE) {
            int port;
            try {
                port = Integer.parseInt(sb.toString());
            } catch (Exception ex) {
                throw new HeartBeatRequestParserException(INVALID_PORT);
            }
            if (port > MAX_PORT_NUMBER)
                throw new HeartBeatRequestParserException(HeartBeatRequestParserError.MAX_PORT_NUMBER);

            if (port < MIN_PORT_NUMBER)
                throw new HeartBeatRequestParserException(HeartBeatRequestParserError.MIN_PORT_NUMBER);

            return port;
        }


        if (!Character.isDigit((char) nextByte))
            throw new HeartBeatRequestParserException(INVALID_PORT);

        this.numberOfPort++;
        sb.append((char) nextByte);
        return 0;
    }

    private String entryFound(byte nextByte) {
        if (numberOfEntry == 0) clearStringBuffer();

        if (this.numberOfEntry > MAX_ENTRY) {
            if (nextByte == CLOSING_BRACE)
                return sb.toString();
            throw new HeartBeatRequestParserException(HeartBeatRequestParserError.MAX_ENTRY);
        }

        if (nextByte == CLOSING_BRACE)
            return sb.toString();

        this.numberOfEntry++;
        sb.append((char) nextByte);
        return null;
    }


    private String hostFound(byte nextByte) {
        if (this.numberOfHost == 0) clearStringBuffer();

        if (this.numberOfHost > MAX_HOST_NAME_LENGTH) {
            if (nextByte == COMMA)
                return sb.toString();
            throw new HeartBeatRequestParserException(MAX_HOST_NAME);
        }

        if (nextByte == COMMA) {
            if (numberOfHost < MIN_HOST_NAME_LENGTH)
                throw new HeartBeatRequestParserException(MIN_HOST_NAME);
            return sb.toString();
        }

        this.numberOfHost++;
        sb.append((char) nextByte);
        return null;
    }


    private boolean modelBeginningFound(byte nextByte) {
        if (nextByte != OPENING_BRACE)
            throw new HeartBeatRequestParserException(INVALID_MODEL_START_TOKEN);
        return true;
    }

    private boolean startTokenFound(byte nextByte) {
        if (nextByte != START_TOKENS[startTokenCounter])
            throw new HeartBeatRequestParserException(INVALID_START_TOKEN);

        startTokenCounter++;

        return startTokenCounter == START_TOKENS.length;
    }

    private ByteBuffer readToBuffer(ReadableByteChannel channel) {
        var buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        try {
            channel.read(buffer);
        } catch (IOException e) {
            throw new HeartBeatRequestParserException(SERVER_ERROR);
        }
        buffer.flip();
        return buffer;
    }
}
