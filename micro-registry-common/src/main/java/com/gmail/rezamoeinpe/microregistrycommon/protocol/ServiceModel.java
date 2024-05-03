package com.gmail.rezamoeinpe.microregistrycommon.protocol;

import com.gmail.rezamoeinpe.microregistrycommon.exception.ServiceModelValidationException;
import com.gmail.rezamoeinpe.microregistrycommon.utils.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

import static com.gmail.rezamoeinpe.microregistrycommon.exception.ServiceModelValidationException.ServiceModelValidationError.*;

public record ServiceModel(String serviceName, String host, int port, String entry) {
    public final static byte MAX_SERVICE_NAME_LENGTH = 30;
    public final static byte MIN_SERVICE_NAME_LENGTH = 2;

    public final static byte MAX_HOST_LENGTH = 30;
    public final static byte MIN_HOST_LENGTH = 2;

    public final static int MAX_PORT_NUMBER = 65535;// 2^16-1
    public final static byte MIN_PORT_NUMBER = 1;

    public final static byte MAX_ENTRY_LENGTH = 100;

    public ServiceModel {
        if (serviceName == null)
            throw new ServiceModelValidationException(SERVICE_NAME_REQUIRED);

        if (StringUtils.isNotPureAscii(serviceName))
            throw new ServiceModelValidationException(NON_US_ASCII_SERVICE_NAME);

        if (!Character.isLetter(serviceName.charAt(0)))
            throw new ServiceModelValidationException(INVALID_SERVICE_NAME_BEGINNING);

        if (!StringUtils.isLetterDigitOrDash(serviceName))
            throw new ServiceModelValidationException(INVALID_SERVICE_NAME);

        if (serviceName.length() > MAX_SERVICE_NAME_LENGTH)
            throw new ServiceModelValidationException(MAX_SERVICE_NAME_EXCEEDED);

        if (serviceName.length() < MIN_SERVICE_NAME_LENGTH)
            throw new ServiceModelValidationException(MIN_SERVICE_NAME_NOT_PROVIDED);

        if (host == null)
            throw new ServiceModelValidationException(HOST_REQUIRED);

        if (host.length() > MAX_HOST_LENGTH)
            throw new ServiceModelValidationException(MAX_HOST_EXCEEDED);

        if (host.length() < MIN_HOST_LENGTH)
            throw new ServiceModelValidationException(MIN_HOST_NOT_PROVIDED);

        if (StringUtils.isNotPureAscii(host))
            throw new ServiceModelValidationException(NON_US_ASCII_HOST);


        if (port > MAX_PORT_NUMBER)
            throw new ServiceModelValidationException(MAX_PORT_EXCEEDED);

        if (port < MIN_PORT_NUMBER)
            throw new ServiceModelValidationException(MIN_PORT_NOT_PROVIDED);

        if (entry != null && entry.length() > MAX_ENTRY_LENGTH)
            throw new ServiceModelValidationException(MAX_ENTRY_EXCEEDED);

    }

    public ServiceModel(String serviceName, String host, int port) {
        this(serviceName, host, port, null);
    }

    public byte[] byteInfo() {
        var joiner = new StringJoiner(",", "{", "}")
                .add(serviceName())
                .add(host())
                .add(String.valueOf(port()));
        if (entry() != null)
            joiner.add(entry());
        return joiner.toString().getBytes(StandardCharsets.US_ASCII);
    }

    public enum ServiceStatus {
        ACTIVE,
        IDLE,
        UNAVAILING
    }
}
