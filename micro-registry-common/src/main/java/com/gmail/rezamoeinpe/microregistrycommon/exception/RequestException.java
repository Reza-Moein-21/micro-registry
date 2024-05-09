package com.gmail.rezamoeinpe.microregistrycommon.exception;

public class RequestException extends ServiceException {

    public RequestException(RequestError error) {
        super(error.name());
    }

    public enum RequestError {
        NOT_SUPPORTED, BAD_REQUEST
    }
}
