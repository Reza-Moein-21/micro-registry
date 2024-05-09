package com.gmail.rezamoeinpe.microregistrycommon.exception;

public class HeartBeatRequestParserException extends ServiceException {

    public HeartBeatRequestParserException(HeartBeatRequestParserError error) {
        super(error.name());
    }

    public enum  HeartBeatRequestParserError {
        INVALID_START_TOKEN,
        INVALID_END_TOKEN,
        INVALID_MODEL_START_TOKEN,
        INVALID_SERVICE_NAME_BEGINNING,
        INVALID_SERVICE_NAME,
        MAX_SERVICE_NAME,
        MIN_SERVICE_NAME,
        MAX_HOST_NAME,
        MIN_HOST_NAME,
        INVALID_PORT,
        MAX_PORT_NUMBER,
        MIN_PORT_NUMBER,
        MAX_ENTRY,
    }
}
