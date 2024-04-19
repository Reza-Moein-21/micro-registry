package com.gmail.rezamoeinpe.microregistrycommon.exception;

public abstract class ServiceException extends RuntimeException {
    private final String code;

    public ServiceException(String code) {
        this.code = code;
    }

    public ServiceException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
