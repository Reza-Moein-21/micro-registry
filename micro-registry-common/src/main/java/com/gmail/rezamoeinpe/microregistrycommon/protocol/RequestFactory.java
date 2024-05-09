package com.gmail.rezamoeinpe.microregistrycommon.protocol;

import com.gmail.rezamoeinpe.microregistrycommon.exception.RequestException;
import com.gmail.rezamoeinpe.microregistrycommon.exception.ServiceException;
import com.gmail.rezamoeinpe.microregistrycommon.protocol.heartbeat.HeartBeatRequestParser;
import com.gmail.rezamoeinpe.microregistrycommon.utils.IOUtils;

import java.nio.channels.ReadableByteChannel;

import static com.gmail.rezamoeinpe.microregistrycommon.exception.RequestException.RequestError.BAD_REQUEST;
import static com.gmail.rezamoeinpe.microregistrycommon.exception.RequestException.RequestError.NOT_SUPPORTED;

public class RequestFactory {
    private static final int MAX_REQUEST_BUFFER_SIZE = 1024;

    public AbstractRequest byParsing(ReadableByteChannel o) {
        if (o == null)
            throw new RequestException(BAD_REQUEST);

        var buffer = IOUtils.readToBuffer(o, MAX_REQUEST_BUFFER_SIZE);
        try {
            return new HeartBeatRequestParser().parser(buffer);
        } catch (ServiceException ignored) {
        }

        throw new RequestException(NOT_SUPPORTED);
    }
}
