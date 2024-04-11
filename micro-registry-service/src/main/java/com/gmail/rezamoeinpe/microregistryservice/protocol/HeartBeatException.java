package com.gmail.rezamoeinpe.microregistryservice.protocol;

public class HeartBeatException extends Exception {
    private final HeartBeatParserError error;

    public HeartBeatException(HeartBeatParserError error) {
        this.error = error;
    }

    public HeartBeatException(HeartBeatParserError error, String message) {
        super(message);
        this.error = error;
    }

    public HeartBeatParserError getError() {
        return error;
    }
}
