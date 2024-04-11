package com.gmail.rezamoeinpe.microregistryservice.protocol;

public enum HeartBeatParserError {
    SERVER_ERROR,
    INVALID_START_TOKEN,
    INVALID_END_TOKEN,
    INVALID_SERVICE_NAME_BEGINNING,
    INVALID_SERVICE_NAME,
    MAX_SERVICE_NAME,
    MIN_SERVICE_NAME
}
