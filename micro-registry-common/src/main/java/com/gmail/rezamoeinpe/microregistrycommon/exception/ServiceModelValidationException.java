package com.gmail.rezamoeinpe.microregistrycommon.exception;

public class ServiceModelValidationException extends ServiceException {

    public ServiceModelValidationException(ServiceModelValidationError error) {
        super(error.name());
    }

    public enum  ServiceModelValidationError {
        SERVICE_NAME_REQUIRED,
        NON_US_ASCII_SERVICE_NAME,
        INVALID_SERVICE_NAME_BEGINNING,
        INVALID_SERVICE_NAME,
        MAX_SERVICE_NAME_EXCEEDED,
        MIN_SERVICE_NAME_NOT_PROVIDED,

        HOST_REQUIRED,
        NON_US_ASCII_HOST,
        MAX_HOST_EXCEEDED,
        MIN_HOST_NOT_PROVIDED,

        MAX_PORT_EXCEEDED,
        MIN_PORT_NOT_PROVIDED,

        MAX_ENTRY_EXCEEDED,

    }
}
