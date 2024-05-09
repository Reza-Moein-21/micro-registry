package com.gmail.rezamoeinpe.microregistrycommon.exception;

public final class ServerErrorException extends ServiceException {
    public final static String SERVER_ERROR = "SERVER_ERROR";

    public ServerErrorException() {
        super(SERVER_ERROR);
    }

    public ServerErrorException(String message) {
        super(SERVER_ERROR, message);
    }
}
