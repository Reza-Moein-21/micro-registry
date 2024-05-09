package com.gmail.rezamoeinpe.microregistrycommon.protocol;

import com.gmail.rezamoeinpe.microregistrycommon.exception.ServiceException;

import java.nio.ByteBuffer;

public interface Parser<T extends AbstractRequest> {
    T parser(ByteBuffer buffer) throws ServiceException;
}
