package com.gmail.rezamoeinpe.microregistrycommon.protocol;

import com.gmail.rezamoeinpe.microregistrycommon.exception.ServiceException;

import java.nio.channels.ReadableByteChannel;

public interface Parser<T> {
    T parser(ReadableByteChannel channel) throws ServiceException;
}
