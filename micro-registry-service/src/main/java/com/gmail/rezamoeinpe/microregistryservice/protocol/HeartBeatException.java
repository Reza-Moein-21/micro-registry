package com.gmail.rezamoeinpe.microregistryservice.protocol;

import com.gmail.rezamoeinpe.microregistrycommon.exception.ServiceException;

public final class HeartBeatException extends ServiceException {
    private final HeartBeatParserError error;

    public HeartBeatException(HeartBeatParserError error) {
        super(error.name());
        this.error = error;
    }

    public HeartBeatException(HeartBeatParserError error, String message) {
        super(error.name(), message);
        this.error = error;
    }

    public HeartBeatParserError getError() {
        return error;
    }
}
